package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.transaction.TransactionDetailHistoriesDto;
import ticket_online.ticket_online.dto.transaction.TransactionHistoriesDto;
import ticket_online.ticket_online.service.HistoriesTransactionService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/transaction-histories")
public class HistoriesTransactionController {

    @Autowired
    private HistoriesTransactionService historiesTransactionService;

    @GetMapping("/histories/{userId}")
    public ResponseEntity<ApiResponse<List<TransactionHistoriesDto>>> transactionHistories(@PathVariable Long userId){
        try {
            List<TransactionHistoriesDto> transactionHistoriesDto = historiesTransactionService.transactionHistories(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "transaction histories", transactionHistoriesDto));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/histories-detail/{transactionCode}/detail-user")
    public ResponseEntity<ApiResponse<TransactionDetailHistoriesDto>> transactionDetailHistories(@PathVariable String transactionCode){
        try {
            TransactionDetailHistoriesDto transactionDetailHistories = historiesTransactionService.transactionDetailHistories(transactionCode);
            return  ResponseEntity.ok(new ApiResponse<>(true, "fsd", transactionDetailHistories));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<Map<String, Object>>> listTransactionAdmin(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                                  @RequestParam(value = "size",defaultValue = "5") int size){
        try {
            Map<String, Object> transactionDetailHistories = historiesTransactionService.allOfTransactionFromUsersAdmin(page,size);
            return  ResponseEntity.ok(new ApiResponse<>(true, "list transactions", transactionDetailHistories));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }



}
