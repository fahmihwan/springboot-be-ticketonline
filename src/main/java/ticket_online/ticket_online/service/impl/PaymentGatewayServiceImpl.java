package ticket_online.ticket_online.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticket_online.ticket_online.client.PaymentGatewayClient;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.transaction.GetPaymentMethodReqDto;
import ticket_online.ticket_online.service.PaymentGatewayService;
import ticket_online.ticket_online.util.GenerateUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PaymentGatewayServiceImpl implements PaymentGatewayService {

    private static final String MERCHANT_CODE = "DS21299";
    private static final String API_KEY = "36d359f7edace8b47b99c6b733eb0313";

    @Autowired
    private PaymentGatewayClient paymentGatewayClient;

    @Override
    public ApiResponse<Map<String, Object>> getPaymentFee(GetPaymentMethodReqDto request) { // opsi pembayaran

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        // GENERATE SIGNATURE KEY
        String dataBefotSignature  = MERCHANT_CODE + request.getAmount() + formattedDateTime + API_KEY;
        String signature = GenerateUtil.generateSignatureKeySHA256(dataBefotSignature);

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("merchantcode", MERCHANT_CODE);
            params.put("amount", request.getAmount());
            params.put("datetime", formattedDateTime);
            params.put("signature", signature);

            Map<String, Object> response = paymentGatewayClient.getPaymentFee(params);
            return new ApiResponse<>(true, "Get Payment retrieved successfully", response);

        }catch (RuntimeException e){
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<Map<String,Object>> transactionRequest(){ //tembak ke pembayaran, untuk mendapatkan payment url

        String merchantCode = MERCHANT_CODE;
        String apiKey = API_KEY;
        int paymentAmount = 40000;
        String paymentMethod = "BC";
        int merchantOrderId = 121212; //dari merchant, unik
        String productDetails = "Tes pembayaran menggunakan Duitku";
        String email = "fahmiiwan86@gmail.com";
        String phoneNumber = "082334337393";
        String additionalParam = ""; // opsional
        String merchantUserInfo = ""; // opsional
        String customerVaName = "John Doe"; // tampilan nama pada tampilan konfirmasi bank
        String callbackUrl = "http://example.com/callback"; // url untuk callback
        String returnUrl = "http://example.com/return"; // url untuk redirect
        Integer expiryPeriod = 10; // atur waktu kadaluarsa dalam hitungan menit

        String dataBefotSignature  = merchantCode + merchantOrderId + paymentAmount + apiKey;
        String signature = GenerateUtil.generateSignatureKeyMD5(dataBefotSignature);

        // Customer Detail
        String firstName = "John";
        String lastName = "Doe";

        // Address
        String alamat = "Jl. Kembangan Raya";
        String city = "Jakarta";
        String postalCode = "11530";
        String countryCode = "ID";

        Map<String, Object> address = new HashMap<>();
        address.put("firstName", firstName);
        address.put("lastName",lastName);
        address.put("address",alamat);
        address.put("city", city);
        address.put("postalCode", postalCode);
        address.put("phone",phoneNumber);
        address.put("countryCode", countryCode);

        Map<String,Object> customerDetail = new HashMap<>();
        customerDetail.put("firstName",firstName);
        customerDetail.put("lastName", lastName);
        customerDetail.put("email", email);
        customerDetail.put("phoneNumber",phoneNumber);
        customerDetail.put("billingAddress", address);
        customerDetail.put("shippingAddress",address);

        Map<String,Object> item1 = new HashMap<>();
        item1.put("name","Test Item 1");
        item1.put("price",10000);
        item1.put("quantity",1);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("name","Test Item 2");
        item2.put("price",30000);
        item2.put("quantity",1);


        List<Object> itemDetails = new ArrayList<>();
        itemDetails.add(item1);
        itemDetails.add(item2);

        Map<String, Object> params = new HashMap<>();
        params.put("merchantcode", merchantCode);
        params.put("paymentAmount", paymentAmount);
        params.put("paymentMethod",paymentMethod);
        params.put("merchantOrderId",merchantOrderId);
        params.put("productDetails",productDetails);
        params.put("additionalParam",additionalParam);
        params.put("merchantUserInfo",merchantUserInfo);
        params.put("customerVaName",customerVaName);
        params.put("email",email);
        params.put("phoneNumber", phoneNumber);
        params.put("itemDetails",itemDetails);
        params.put("customerDetail",customerDetail);
        params.put("callbackUrl", callbackUrl);
        params.put("returnUrls",returnUrl);
        params.put("signature", signature);
        params.put("expiryPeriod", expiryPeriod);
        System.out.println(params);

        try {
            Map<String, Object> response = paymentGatewayClient.transactionRequest(params);
            return new ApiResponse<>(true, "Transaction Request retrieved successfully", response);
        }catch (RuntimeException e) {
            return new ApiResponse<>(true, e.getMessage(), null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public ApiResponse<Map<String, Object>> callbackUrl(){
        String apiKey = API_KEY;
//        String merchantCode =

        return null;
    }





}
