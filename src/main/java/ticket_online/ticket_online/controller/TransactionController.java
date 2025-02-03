package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.transaction.AddCartTicketReqDto;
import ticket_online.ticket_online.dto.transaction.GetPaymentMethodReqDto;
import ticket_online.ticket_online.model.DetailTransaction;
import ticket_online.ticket_online.model.LineUp;
import ticket_online.ticket_online.service.PaymentGatewayService;
import ticket_online.ticket_online.service.TransactionService;

import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private PaymentGatewayService paymentGatewayService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/cart-ticket")
    public ResponseEntity<ApiResponse<DetailTransaction>> storeDetailTransaction(@RequestBody AddCartTicketReqDto addCartTicketReqDto){
        try {
            DetailTransaction response =  transactionService.createCartTicket(addCartTicketReqDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Line up created successfully", response));
        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
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
