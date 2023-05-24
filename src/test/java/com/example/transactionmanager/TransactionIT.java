package com.example.transactionmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.transactionmanager.model.ResponseDTO;
import com.example.transactionmanager.service.TransactionService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransactionIT {

  @Autowired
  private TransactionService transactionService;

  @Test
  public void methodsShouldReturnSameResults() {
    String userId = "4ef83cc2-18f5-4e32-9064-cfe3e05572c0";
    List<ResponseDTO> result1 = transactionService
        .getPreparedTransactionsData_Blocking(userId);

    List<ResponseDTO> result2 = transactionService
        .getPreparedTransactionsData_NonBlocking(userId).block();

    ResponseDTO responseDTO1 = result1.stream().findFirst().orElse(null);
    assert responseDTO1 != null;
    String categoryCode1 = responseDTO1.getCategoryCode();
    BigDecimal averageCost1 = responseDTO1.getAverageCost();

    assert result2 != null;
    ResponseDTO responseDTO2 = result2.stream()
        .filter(responseDTO -> responseDTO.getCategoryCode().equals(categoryCode1)).findFirst()
        .orElse(null);
    assert responseDTO2 != null;
    BigDecimal averageCost2 = responseDTO1.getAverageCost();

    assertEquals(averageCost1, averageCost2);
  }

}
