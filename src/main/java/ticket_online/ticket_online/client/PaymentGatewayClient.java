package ticket_online.ticket_online.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class PaymentGatewayClient {

    public Map<String,Object> getPaymentFee(Map<String, Object> params){

        String url = "https://sandbox.duitku.com/webapi/api/merchant/paymentmethod/getpaymentmethod";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(params, headers);

        // Melakukan POST request ke API Payment Gateway
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        return responseEntity.getBody();
    }

    public Map<String,Object> transactionRequest(Map<String, Object> params){

        String url = "https://sandbox.duitku.com/webapi/api/merchant/v2/inquiry";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(params, headers);

        // Melakukan POST request ke API Payment Gateway
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        return responseEntity.getBody();
    }


}
