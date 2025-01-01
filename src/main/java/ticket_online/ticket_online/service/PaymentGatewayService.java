package ticket_online.ticket_online.service;

import org.springframework.http.ResponseEntity;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.transaction.GetPaymentMethodReqDto;

import java.util.Map;

public interface PaymentGatewayService {

    public ApiResponse<Map<String, Object>> getPaymentFee(GetPaymentMethodReqDto amount);

    public ApiResponse<Map<String,Object>> transactionRequest();

    public ApiResponse<Map<String, Object>> callbackUrl();
}
