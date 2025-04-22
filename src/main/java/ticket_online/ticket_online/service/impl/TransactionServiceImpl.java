package ticket_online.ticket_online.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import ticket_online.ticket_online.client.PaymentGatewayClient;
import ticket_online.ticket_online.dto.transaction.CheckoutReqDto;
import ticket_online.ticket_online.model.*;
import ticket_online.ticket_online.repository.*;
import ticket_online.ticket_online.service.TransactionService;
import ticket_online.ticket_online.util.GenerateUtil;

import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final String MERCHANT_CODE = "DS21299";
    private static final String API_KEY = "36d359f7edace8b47b99c6b733eb0313";

    @Autowired
    private PaymentGatewayClient paymentGatewayClient;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private DetailTransactionRepository detailTransactionRepository;

    @Autowired
    private CategoryTicketRepository categoryTicketRepository;


    @Transactional
    @Override
    public Map<String,Object> checkout(CheckoutReqDto checkoutReqDto){ //tembak ke pembayaran, untuk mendapatkan payment url

        try {
            List<CheckoutReqDto.Participans> participansPayload = checkoutReqDto.getParticipants();
            List<CheckoutReqDto.DetailCarts> detailCartsPayload = checkoutReqDto.getDetailCartTicket();

            Transaction transaction1 = new Transaction();
            transaction1.setTransactionCode(GenerateUtil.transactionCode());
            transactionRepository.save(transaction1);

            Long primaryVisitorId = null;
            int total_price = 0;
            int total_qty = 0;

            for (int i = 0; i < participansPayload.size(); i++) {
                Visitor visitor = new Visitor();
                DetailTransaction detailTransaction = new DetailTransaction();
                detailTransaction.setTransactionId(transaction1.getId());

              if(i == 0){
                  visitor.setFullName(participansPayload.get(i).getFull_name());
                  visitor.setEmail(participansPayload.get(i).getEmail());
                  visitor.setBirthDate(participansPayload.get(i).getBirth_date());
                  visitor.setGender(participansPayload.get(i).getGender());
                  visitor.setPhoneNumber(participansPayload.get(i).getTelp());
                  visitor.setAddress(participansPayload.get(i).getAddress());
                  visitor.setIsPrimaryVisitor(true);
                  visitorRepository.save(visitor);
                  primaryVisitorId = visitor.getId();
              }else if(i > 0){

                if(participansPayload.get(i).getIs_same_credential()){
                    detailTransaction.setVisitorId(primaryVisitorId);
                }else{
                    visitor.setFullName(participansPayload.get(i).getFull_name());
                    visitor.setEmail(participansPayload.get(i).getEmail());
                    visitor.setBirthDate(participansPayload.get(i).getBirth_date());
                    visitor.setGender(participansPayload.get(i).getGender());
                    visitor.setPhoneNumber(participansPayload.get(i).getTelp());
                    visitor.setAddress(participansPayload.get(i).getAddress());
                    visitor.setIsPrimaryVisitor(false);
                    visitorRepository.save(visitor);

                    detailTransaction.setVisitorId(visitor.getId());
                }
                detailTransaction.setCategoryTicketId(participansPayload.get(i).getCategory_ticket_id());
                detailTransaction.setUserIid(checkoutReqDto.getUserId());
                detailTransactionRepository.save(detailTransaction);
              }
            }


            for (int i = 0; i < detailCartsPayload.size(); i++) {
                Optional<CategoryTicket> categoryTicket = categoryTicketRepository.findById(detailCartsPayload.get(i).getCategory_ticket_id());
                if(categoryTicket.isPresent()){
                    CategoryTicket getCategoryTicket = categoryTicket.get();
                    total_qty += detailCartsPayload.get(i).getTotal();
                    total_price += getCategoryTicket.getPrice() * detailCartsPayload.get(i).getTotal();
                }else{
                    throw new RuntimeException("tiket not available");
                }
            }

            String merchantCode = MERCHANT_CODE;
            String apiKey = API_KEY;

            Integer paymentAmount = total_price;
            String paymentMethod = checkoutReqDto.getPaymentMethod();
            String merchantOrderId = GenerateUtil.generateMerchantOrderId(); //dari merchant, unik
            String productDetails = "Tes pembayaran menggunakan Duitku";
            String email = participansPayload.get(0).getEmail();
            String phoneNumber = participansPayload.get(0).getTelp();
            String additionalParam = ""; // opsional
            String merchantUserInfo = ""; // opsional
            String customerVaName = participansPayload.get(0).getFull_name(); // tampilan nama pada tampilan konfirmasi bank
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
//
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

            List<Object> itemDetails = new ArrayList<>();
            for (int x = 0; x < checkoutReqDto.getDetailCartTicket().size(); x++) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", checkoutReqDto.getDetailCartTicket().get(x).getCategory_name());
                item.put("price", checkoutReqDto.getDetailCartTicket().get(x).getPrice() * checkoutReqDto.getDetailCartTicket().get(x).getTotal());
                item.put("quantity", checkoutReqDto.getDetailCartTicket().get(x).getTotal());
                itemDetails.add(item);
            }


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


            Map<String, Object> response = paymentGatewayClient.transactionRequest(params);
            Optional<Transaction> getTransactionFirst =  transactionRepository.findById(transaction1.getId());

            if(getTransactionFirst.isPresent()){
                Transaction updateTransaction = getTransactionFirst.get();
                updateTransaction.setTotalQty(total_qty);
                updateTransaction.setTotalPrice(total_price);

                updateTransaction.setPgMerchantCode((String) response.get("merchantCode")) ;
                updateTransaction.setPgPaymentReference((String) response.get("reference"));
                updateTransaction.setPgPaymentUrl((String) response.get("paymentUrl"));
                updateTransaction.setPgVaNumber((String) response.get("vaNumber"));
                updateTransaction.setPgAmount(Integer.parseInt(response.get("amount").toString()));
                updateTransaction.setPgStatusCode((String) response.get("statusCode"));
                updateTransaction.setPgStatusMessage((String) response.get("ststatusMessage"));
                transactionRepository.save(updateTransaction);
            }else{
                throw new RuntimeException("transaction not found");
            }

            return response;
        }catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }


    // Custom Exception untuk menangani error dari Duitku
    public static class DuitkuApiException extends RuntimeException {
        private final int statusCode;

        public DuitkuApiException(String message, int statusCode) {
            super(message);
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }
}


