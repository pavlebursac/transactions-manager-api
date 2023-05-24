package com.example.transactionmanager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Transactions Manager API"))
public class TransactionManagerApplication {

  public static void main(String[] args) {
    SpringApplication.run(TransactionManagerApplication.class, args);
  }
}
