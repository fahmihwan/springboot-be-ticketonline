package ticket_online.ticket_online.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;
import ticket_online.ticket_online.constant.ETransactionStatus;
import ticket_online.ticket_online.controller.TransactionController;
import ticket_online.ticket_online.model.Transaction;
import ticket_online.ticket_online.repository.TransactionRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@Component
public class PaymentGatewayClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;
    // Membuat Logger instance untuk kelas ini
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private static final String MERCHANT_CODE = "DS21299";
    private static final String API_KEY = "36d359f7edace8b47b99c6b733eb0313";

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


    public CompletableFuture<Map<String,Object>> transactionRequest(Map<String, Object> params)  {

      try {
          String url = "https://sandbox.duitku.com/webapi/api/merchant/v2/inquiry";

          HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.APPLICATION_JSON);
          HttpEntity<Object> entity = new HttpEntity<>(params, headers);

          ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                  url, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {}
          );
          return CompletableFuture.completedFuture(responseEntity.getBody());
      }catch (Exception e){
          e.printStackTrace();
//          return CompletableFuture.completedFuture(null);
          throw new CompletionException("Gagal call Duitku", e);
      }


    }



    public String handleCallbackPayament(Map<String, String> params) {


        String merchantCode = String.valueOf(params.get("merchantCode"));
        String amount = String.valueOf(params.get("amount"));
        String merchantOrderId = String.valueOf(params.get("merchantOrderId"));
        String signature = String.valueOf(params.get("signature"));
        String resultCode = String.valueOf(params.get("resultCode"));
        String publisherOrderId = String.valueOf(params.get("publisherOrderId"));
        String settlementDateString = String.valueOf(params.get("settlementDate"));
        LocalDate settlementDate = LocalDate.parse(settlementDateString);
        String issuerCode = String.valueOf(params.get("issuerCode"));

        if (merchantCode == null || amount == null || merchantOrderId == null || signature == null) {
            throw new IllegalArgumentException("Bad Parameter");
        }

        String rawParams = merchantCode + amount + merchantOrderId + API_KEY;
        String calcSignature = DigestUtils.md5DigestAsHex(rawParams.getBytes(StandardCharsets.UTF_8));

        if (!signature.equalsIgnoreCase(calcSignature)) {
            throw new IllegalArgumentException("Bad Signature");
        }

       Optional<Transaction> transaction = transactionRepository.findFirstByPgMerchantOrderId(merchantOrderId);
        if(transaction.isPresent()){
            Transaction transaction1= transaction.get();
            if(resultCode.equals("00")){ //success
                transaction1.setTransactionStatus(ETransactionStatus.SUCCESS);
            }else if(resultCode.equals("01")){ //failed
                transaction1.setTransactionStatus(ETransactionStatus.FAILED);
            }

            transaction1.setPgStatusCode(resultCode);
            transaction1.setPgSettlementDate(settlementDate);
            transaction1.setPgPublisherOrderId(publisherOrderId);
            transaction1.setIssuerCode(issuerCode);
            transactionRepository.save(transaction1);
        }

        logger.info("log for "+ merchantOrderId);


        // Signature valid - lakukan proses update status transaksi dsb.
        System.out.println("✅ Callback validated!");
        System.out.println("Merchant Order ID: " + merchantOrderId);
        // ... proses lainnya seperti update database dll.

        return "OK";
    }
}
