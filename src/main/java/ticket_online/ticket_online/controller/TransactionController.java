package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.transaction.CheckoutReqDto;
import ticket_online.ticket_online.dto.transaction.GetPaymentMethodReqDto;
import ticket_online.ticket_online.service.PaymentGatewayService;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private PaymentGatewayService paymentGatewayService;


    private static final String MERCHANT_CODE = "DS21299";
    private static final String API_KEY = "36d359f7edace8b47b99c6b733eb0313x";

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
    public ResponseEntity<ApiResponse<Map<String, Object>>> transactionRequest(@RequestBody CheckoutReqDto checkoutReqDto){

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
