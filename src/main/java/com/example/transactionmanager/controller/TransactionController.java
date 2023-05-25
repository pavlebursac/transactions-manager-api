package com.example.transactionmanager.controller;


import com.example.transactionmanager.model.ResponseDTO;
import com.example.transactionmanager.service.TransactionService;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;

  @GetMapping("/costs-blocking/{userId}")
  public List<ResponseDTO> getCostsBlocking(
      @Parameter(name = "userId", description = "example: 4ef83cc2-18f5-4e32-9064-cfe3e05572c0")
      @PathVariable("userId") String userId) {
    return transactionService.getPreparedTransactionsData_Blocking(userId);
  }

  @GetMapping("/costs-non-blocking/{userId}")
  public Mono<List<ResponseDTO>> getCostsNonBlocking(
      @Parameter(name = "userId", description = "example: 4ef83cc2-18f5-4e32-9064-cfe3e05572c0")
      @PathVariable("userId") String userId) {
    return transactionService
        .getPreparedTransactionsData_NonBlocking(userId);
  }
}
