package com.example.transactionmanager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.transactionmanager.client.TransactionsClient;
import com.example.transactionmanager.model.DataDTO;
import com.example.transactionmanager.model.LinksDTO;
import com.example.transactionmanager.model.ResponseDTO;
import com.example.transactionmanager.model.TransactionDTO;
import com.example.transactionmanager.model.TransactionSubClassDTO;
import com.example.transactionmanager.service.AuthenticationService;
import com.example.transactionmanager.service.TransactionServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


@ExtendWith(MockitoExtension.class)
public class TransactionTest {

  @Mock
  private AuthenticationService authenticationService;
  @Mock
  private TransactionsClient transactionsClient;
  @InjectMocks
  private TransactionServiceImpl transactionService;

  private DataDTO dataDTO;

  @BeforeEach
  public void init() {
    ReflectionTestUtils
        .setField(transactionService, "transactionsUrl", "https://example.com/transactions");

    prepareTestData();
  }

  @Test
  public void testMethodResponse() {

    when(authenticationService.getToken()).thenReturn("some_token_value");
    when(transactionsClient.getTransactionsRequest_Blocking(any(), eq("some_token_value")))
        .thenReturn(dataDTO);

    final List<ResponseDTO> result = transactionService
        .getPreparedTransactionsData_Blocking("some_user_uuid");

    ResponseDTO responseDTO = result.stream().findFirst().orElse(null);
    assert responseDTO != null;
    String categoryName = responseDTO.getCategoryName();
    BigDecimal averageCost = responseDTO.getAverageCost();

    assertThat(categoryName).isEqualTo("some_category");
    assertThat(averageCost).isEqualTo(BigDecimal.valueOf(1.50).setScale(2));
  }

  private void prepareTestData() {
    List<TransactionDTO> transactionDTOs = new ArrayList<>();

    TransactionSubClassDTO subClassDTO = new TransactionSubClassDTO();
    subClassDTO.setTitle("some_category");
    subClassDTO.setCode("some_code");

    TransactionDTO transactionDTO1 = new TransactionDTO();
    transactionDTO1.setAmount(BigDecimal.valueOf(1));
    transactionDTO1.setSubClassDTO(subClassDTO);

    TransactionDTO transactionDTO2 = new TransactionDTO();
    transactionDTO2.setAmount(BigDecimal.valueOf(2));
    transactionDTO2.setSubClassDTO(subClassDTO);

    transactionDTOs.add(transactionDTO1);
    transactionDTOs.add(transactionDTO2);

    dataDTO = new DataDTO();
    dataDTO.setData(transactionDTOs);
    dataDTO.setLinks(new LinksDTO());
  }

}
