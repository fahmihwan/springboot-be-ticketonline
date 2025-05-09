package ticket_online.ticket_online.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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

    @Async
    public CompletableFuture<Map<String,Object>> transactionRequest(Map<String, Object> params)  {

      try {
          String url = "https://sandbox.duitku.com/webapi/api/merchant/v2/inquiry";

          RestTemplate restTemplate = new RestTemplate();
          HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.APPLICATION_JSON);
          HttpEntity<Object> entity = new HttpEntity<>(params, headers);

          ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                  url, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {}
          );
          return CompletableFuture.completedFuture(responseEntity.getBody());
      }catch (Exception e){

          return CompletableFuture.completedFuture(null);
      }


    }
}
