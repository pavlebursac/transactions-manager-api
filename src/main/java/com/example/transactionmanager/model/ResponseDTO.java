package com.example.transactionmanager.model;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDTO {

  String categoryName;
  String categoryCode;
  BigDecimal totalCosts;
  BigDecimal averageCost;
  int totalTransactions;
}
