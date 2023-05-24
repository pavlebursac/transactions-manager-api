package com.example.transactionmanager.service;

import com.example.transactionmanager.client.TransactionsClient;
import com.example.transactionmanager.model.DataDTO;
import com.example.transactionmanager.model.ResponseDTO;
import com.example.transactionmanager.model.TransactionDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  @Value("${transactions.url}")
  private String transactionsUrl;

  private final TransactionsClient transactionsClient;
  private final AuthenticationService authenticationService;
  private final String RESPONSE_LIMIT = "&limit=300";

  public List<ResponseDTO> getPreparedTransactionsData_Blocking(String userId) {
    String token = authenticationService.getToken();

    String url = transactionsUrl.replace("{userId}", userId) + RESPONSE_LIMIT;

    List<DataDTO> dataDTOList = new ArrayList<>();
    do {
      DataDTO dataDTO = transactionsClient
          .getTransactionsRequest_Blocking(url, token);
      dataDTOList.add(dataDTO);
      assert dataDTO != null;
      url = dataDTO.getLinks().getNext();
    } while (url != null);

    List<ResponseDTO> result = new ArrayList<>();
    for (DataDTO dataDTO : dataDTOList) {
      calculateCosts(dataDTO, result);
    }
    return result;
  }

  public Mono<List<ResponseDTO>> getPreparedTransactionsData_NonBlocking(String userId) {
    String token = authenticationService.getToken();
    String url = transactionsUrl.replace("{userId}", userId) + RESPONSE_LIMIT;
    List<ResponseDTO> result = new ArrayList<>();
    return transactionsClient.getTransactionsRequest_NonBlocking(url, token)
        .expand(dataDTO -> {
          calculateCosts(dataDTO, result);
          String nextLink = dataDTO.getLinks().getNext();
          if (nextLink == null) {
            return Mono.empty();
          } else {
            return transactionsClient
                .getTransactionsRequest_NonBlocking(nextLink + RESPONSE_LIMIT, token);
          }
        })
        .last()
        .map(dataDTO -> result);
  }

  private void calculateCosts(DataDTO dataDTO, List<ResponseDTO> responseDTOList) {
    for (TransactionDTO transactionDTO : dataDTO.getData()) {
      String categoryName = transactionDTO.getSubClassDTO().getTitle();
      BigDecimal newAmount = transactionDTO.getAmount();
      String categoryCode = transactionDTO.getSubClassDTO().getCode();
      Optional<ResponseDTO> existingResponse = responseDTOList.stream()
          .filter(responseDTO -> responseDTO.getCategoryCode().equals(categoryCode))
          .findFirst();

      if (existingResponse.isPresent()) {
        ResponseDTO responseDTO = existingResponse.get();
        int transactionsCount = responseDTO.getTotalTransactions() + 1;
        responseDTO.setTotalTransactions(transactionsCount);
        responseDTO.setTotalCosts(responseDTO.getTotalCosts().add(newAmount));
      } else {
        ResponseDTO newResponseDTO = new ResponseDTO();
        newResponseDTO.setCategoryName(categoryName);
        newResponseDTO.setCategoryCode(categoryCode);
        newResponseDTO.setTotalTransactions(1);
        newResponseDTO.setTotalCosts(newAmount);
        responseDTOList.add(newResponseDTO);
      }
    }
    calculateAverageCosts(responseDTOList);
  }

  private void calculateAverageCosts(List<ResponseDTO> responseDTOList) {
    responseDTOList.forEach(responseDTO -> {
      BigDecimal totalCosts = responseDTO.getTotalCosts();
      BigDecimal totalTransactions = BigDecimal.valueOf(responseDTO.getTotalTransactions());
      BigDecimal averageCost = totalCosts.divide(totalTransactions, 2, RoundingMode.HALF_UP);
      responseDTO.setAverageCost(averageCost);
    });
  }
}
