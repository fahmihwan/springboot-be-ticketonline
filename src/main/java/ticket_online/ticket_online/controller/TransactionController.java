package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.cart.AddCartTicketReqDto;
import ticket_online.ticket_online.dto.transaction.CheckoutReqDto;
import ticket_online.ticket_online.dto.transaction.GetPaymentMethodReqDto;
import ticket_online.ticket_online.dto.transaction.TransactionDetailHistoriesDto;
import ticket_online.ticket_online.dto.transaction.TransactionHistoriesDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.Transaction;
import ticket_online.ticket_online.service.PaymentGatewayService;
import ticket_online.ticket_online.service.TransactionService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private PaymentGatewayService paymentGatewayService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/checkout")
//    public ResponseEntity<ApiResponse<CheckoutReqDto>> checkout(@RequestBody CheckoutReqDto checkoutReqDto){
    public ResponseEntity<ApiResponse<?>> checkout(@RequestBody CheckoutReqDto checkoutReqDto){
        try {

            Map<String,Object> response = transactionService.checkout(checkoutReqDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event Detail retrieved", response));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/histories/{userId}")
    public ResponseEntity<ApiResponse<List<TransactionHistoriesDto>>> transactionHistories(@PathVariable Long userId){
        try {
            List<TransactionHistoriesDto> transactionHistoriesDto = transactionService.transactionHistories(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "transaction histories", transactionHistoriesDto));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/histories-detail/{transactionCode}")
    public ResponseEntity<ApiResponse<TransactionDetailHistoriesDto>> transactionDetailHistories(@PathVariable String transactionCode){
        try {
            TransactionDetailHistoriesDto transactionDetailHistories = transactionService.transactionDetailHistories(transactionCode);
            return  ResponseEntity.ok(new ApiResponse<>(true, "fsd", transactionDetailHistories));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @PostMapping("/paymentgateway-get-payment-method")
    public ResponseEntity<ApiResponse<Map<String,Object>>> getPaymentMethod(@RequestBody GetPaymentMethodReqDto request) {
        ApiResponse<Map<String,Object>> response =   paymentGatewayService.getPaymentFee(request);
       if(response.getSuccess()){
           return ResponseEntity.ok(response);
       }else{
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
       }
    }

    @PostMapping("/paymentgateway-transaction-request")
    public ResponseEntity<ApiResponse<Map<String, Object>>> transactionRequest(@RequestBody AddCartTicketReqDto checkoutReqDto){

        ApiResponse<Map<String,Object>> response = paymentGatewayService.transactionRequest();
        return ResponseEntity.ok(response);

    }

    @PostMapping("/paymentgateway-callbackurl")
    public ResponseEntity<ApiResponse<Map<String, Object>>> callbackUrl(@RequestBody Map<String,Object> request){
        ApiResponse<Map<String, Object>> response = paymentGatewayService.callbackUrl();
        if(response.getSuccess()){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}
