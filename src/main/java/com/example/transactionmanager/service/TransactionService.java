package com.example.transactionmanager.service;

import com.example.transactionmanager.model.ResponseDTO;
import java.util.List;
import reactor.core.publisher.Mono;

public interface TransactionService {

  List<ResponseDTO> getPreparedTransactionsData_Blocking(String userId);

  Mono<List<ResponseDTO>> getPreparedTransactionsData_NonBlocking(String userId);
}
