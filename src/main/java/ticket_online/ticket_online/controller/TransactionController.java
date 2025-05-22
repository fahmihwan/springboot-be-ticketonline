package ticket_online.ticket_online.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.client.PaymentGatewayClient;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.cart.AddCartTicketReqDto;
import ticket_online.ticket_online.dto.transaction.CheckoutReqDto;
import ticket_online.ticket_online.dto.transaction.GetPaymentMethodReqDto;
import ticket_online.ticket_online.service.PaymentGatewayService;
import ticket_online.ticket_online.service.TransactionService;

import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {


    // Membuat Logger instance untuk kelas ini
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private PaymentGatewayService paymentGatewayService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PaymentGatewayClient paymentGatewayClient;

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<?>> checkout(@RequestBody CheckoutReqDto checkoutReqDto){
        try {

            Map<String,Object> response = transactionService.checkout(checkoutReqDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event Detail retrieved", response));
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

    @GetMapping("/check-transaction-exists/{userId}/{slug}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> checkUserExcheckIfCurrentTransactionExists(@PathVariable Long userId, @PathVariable String slug){
        try {
            List<Map<String,Object>> response =  transactionService.checkIfCurrentTransactionEventForUserExists(slug, userId);
            return  ResponseEntity.ok(new ApiResponse<>(true, "fsd", response));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{transactionCode}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelledTransaction(@PathVariable String transactionCode){
        try {
            transactionService.cancelledTransaction(transactionCode);
            return  ResponseEntity.ok(new ApiResponse<>(true, "canceled transaction successfully", transactionCode));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/callback")
    public ResponseEntity<ApiResponse<String>> callback(@RequestParam Map<String, String> body){
        try {
           String handleCallback = transactionService.handleCallbackPayment(body);
            return  ResponseEntity.ok(new ApiResponse<>(true, "callback successfully", handleCallback));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }































}
