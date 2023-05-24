package com.example.transactionmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionDTO {

  String type;
  BigDecimal amount;
  @JsonProperty("class")
  String transactionClass;
  @JsonProperty("subClass")
  TransactionSubClassDTO subClassDTO;
}
