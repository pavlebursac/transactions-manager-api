package com.example.transactionmanager.client;

import com.example.transactionmanager.model.DataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TransactionsClient {

  public DataDTO getTransactionsRequest_Blocking(String url, String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<String> entity = new HttpEntity<>("body", headers);

    ResponseEntity<DataDTO> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        entity,
        DataDTO.class
    );
    return response.getBody();
  }

  public Mono<DataDTO> getTransactionsRequest_NonBlocking(String url, String token) {
    return WebClient.builder()
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .build()
        .method(HttpMethod.GET)
        .uri(url)
        .retrieve()
        .bodyToMono(DataDTO.class);
  }
}
