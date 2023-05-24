package com.example.transactionmanager.model;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataDTO {

  int count;
  int size;
  List<TransactionDTO> data;
  LinksDTO links;
}
