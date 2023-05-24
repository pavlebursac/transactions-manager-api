package com.example.transactionmanager.service;

import com.example.transactionmanager.model.TokenDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticationService {

  @Value("${authentication.url}")
  private String url;
  @Value("${authentication.api-key}")
  private String apiKey;

  public String getToken() {

    HttpHeaders headers = new HttpHeaders();
    headers.add("scope", "SERVER_ACCESS");
    headers.add("basiq-version", "2.1");
    headers.add("Authorization", "Basic " + apiKey);
    headers.add("Content-Type", "application/x-www-form-urlencoded");
    RestTemplate restTemplate = new RestTemplate();

    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

    ResponseEntity<String> response = restTemplate.exchange(
        url,
        HttpMethod.POST,
        entity,
        String.class
    );
    ObjectMapper objectMapper = new ObjectMapper();
    TokenDTO tokenDTO;
    try {
      tokenDTO = objectMapper.readValue(response.getBody(), TokenDTO.class);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    assert tokenDTO != null;
    return tokenDTO.getToken();
  }
}
