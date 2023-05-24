package com.example.transactionmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenDTO {

  @JsonProperty("access_token")
  String token;
  @JsonProperty("token_type")
  String type;
  @JsonProperty("expires_in")
  int expires;
}
