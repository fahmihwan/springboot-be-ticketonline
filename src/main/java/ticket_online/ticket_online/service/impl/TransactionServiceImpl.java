package ticket_online.ticket_online.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import ticket_online.ticket_online.client.PaymentGatewayClient;
import ticket_online.ticket_online.constant.ETransactionStatus;
import ticket_online.ticket_online.controller.TransactionController;
import ticket_online.ticket_online.dto.transaction.CheckoutReqDto;
import ticket_online.ticket_online.dto.transaction.TransactionDetailHistoriesDto;
import ticket_online.ticket_online.dto.transaction.TransactionHistoriesDto;
import ticket_online.ticket_online.model.*;
import ticket_online.ticket_online.repository.*;
import ticket_online.ticket_online.service.TransactionService;
import ticket_online.ticket_online.util.CheckUtil;
import ticket_online.ticket_online.util.ConvertUtil;
import ticket_online.ticket_online.util.GenerateUtil;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class TransactionServiceImpl implements TransactionService {


//    paymentgateway.duitku.callbackurl="https://d27e-125-165-65-152.ngrok-free.app/api/transaction/callback"
//    paymentgateway.duitku.returnurl="https://www.google.com"
//    ="DS21299"
//    paymentgateway.duitku.apikey="36d359f7edace8b47b99c6b733eb0313"

    @Value("${paymentgateway.duitku.merchantcode}")
    private String MERCHANT_CODE;

    @Value("${paymentgateway.duitku.apikey}")
    private String API_KEY;

    @Value("${paymentgateway.duitku.callbackurl}")
    private String CALLBACK_URL;

    @Value("${paymentgateway.duitku.returnurl}")
    private String RETURN_URL;


    // Membuat Logger instance untuk kelas ini
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

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

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String,Object> checkout(CheckoutReqDto checkoutReqDto){ //tembak ke pembayaran, untuk mendapatkan payment url
        String merchantCode = MERCHANT_CODE;
        try {
            Optional<Event> getEvent = eventRepository.findFirstBySlugAndIsActiveTrue(checkoutReqDto.getSlug());
            if(getEvent.isEmpty()){
                throw new RuntimeException("event not available");
            }

            List<CheckoutReqDto.Participans> participansPayload = checkoutReqDto.getParticipants();
            List<CheckoutReqDto.DetailCarts> detailCartsPayload = checkoutReqDto.getDetailCartTicket();
            String transactionCode = GenerateUtil.transactionCode();
            Transaction transaction1 = new Transaction();
            transaction1.setTransactionCode(transactionCode);

            transaction1.setEventId(getEvent.get());

            transaction1.setPaymentMethod(checkoutReqDto.getPaymentMethod());
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
                    visitor.setIsSamePrimaryVisitor(primaryVisitorId);
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

            Integer paymentAmount = total_price;
            String paymentMethod = checkoutReqDto.getPaymentMethod();
            String merchantOrderId = GenerateUtil.generateMerchantOrderId(); //dari merchant, unik
            String productDetails = "Tes pembayaran menggunakan Duitku";
            String email = participansPayload.get(0).getEmail();
            String phoneNumber = participansPayload.get(0).getTelp();
            String additionalParam = ""; // opsional
            String merchantUserInfo = ""; // opsional
            String customerVaName = participansPayload.get(0).getFull_name(); // tampilan nama pada tampilan konfirmasi bank
            String callbackUrl = CALLBACK_URL; // url untuk callback
            String returnUrl = RETURN_URL; // url untuk redirect
            Integer expiryPeriod = 30; // atur waktu kadaluarsa dalam hitungan menit

            // make signatureKEY
            String dataBefotSignature  = merchantCode + merchantOrderId + paymentAmount + API_KEY;
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


            CompletableFuture<Map<String, Object>> responseFuture = paymentGatewayClient.transactionRequest(params);
            Optional<Transaction> getTransactionFirst =  transactionRepository.findById(transaction1.getId());

            try {
                Map<String, Object> response =  responseFuture.get();

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
                    if(Objects.equals((String) response.get("statusCode"), "00")){
                        updateTransaction.setTransactionStatus(ETransactionStatus.valueOf("PENDING"));
                    }
                    updateTransaction.setPgStatusMessage((String) response.get("ststatusMessage"));
                    updateTransaction.setExpiryPeriod(expiryPeriod);
                    updateTransaction.setUserId(checkoutReqDto.getUserId());

                    updateTransaction.setPgMerchantOrderId(merchantOrderId);
                    updateTransaction.setPgAmount(paymentAmount);
                    response.put("transaction_code", transactionCode);
                    transactionRepository.save(updateTransaction);
                    cartRepository.deleteByUserId(checkoutReqDto.getUserId());
                    return response;
                }else{
                    throw new RuntimeException("transaction not found");
                }

            }catch (Exception e){
                throw new RuntimeException("Payment failed", e);
            }



        }catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }


    @Override
    public List<Map<String, Object>> checkIfCurrentTransactionEventForUserExists(String slug, Long userId){
        String sql = "select t.id,t.transaction_code, t.pg_payment_url, t.created_at,t.expiry_period, t.transaction_status from transactions t \n" +
                "inner join events e on t.event_id = e.id \n" +
                "where e.slug = ? and t.user_id = ? and t.transaction_status = 'PENDING'";
        List<Map<String, Object>> transactionHistories = jdbcTemplate.queryForList(sql, slug, userId);
        System.out.println(transactionHistories);
        List<Map<String, Object>> mp = new ArrayList<>();
        for (Map<String, Object> obj : transactionHistories){

            boolean isExpired = CheckUtil.checkIsExpiredTransaction(((Number) obj.get("expiry_period")).intValue(), (Timestamp) obj.get("created_at"));
            obj.put("is_expired", isExpired);
            if(isExpired){
                String updateSql = "UPDATE transactions SET transaction_status = 'EXPIRED' WHERE id = ?";
                jdbcTemplate.update(updateSql, obj.get("id"));
            }
        }
        return  transactionHistories;
    }

    @Override
    public void updateStatusTransactionByCode(String transactionCode){
        String sql = "select * from transactions t where transaction_code = ? LIMIT 1";
        List<Map<String, Object>> transactions = jdbcTemplate.queryForList(sql, transactionCode);

        for (Map<String, Object> obj : transactions){
            Integer expiryPeriod = ((Number) obj.get("expiry_period")).intValue();
            Timestamp createdAtTimestamp = (Timestamp) obj.get("created_at");

            boolean isExpired =  CheckUtil.checkIsExpiredTransaction(expiryPeriod, createdAtTimestamp);
            obj.put("is_expired", isExpired);
            if(isExpired &&  obj.get("transaction_status").equals("PENDING")){
                String updateSql = "UPDATE transactions SET transaction_status = 'EXPIRED' WHERE id = ?";
                jdbcTemplate.update(updateSql, obj.get("id"));
            }

        }
    }

    @Override
    public void cancelledTransaction(String transactionCode){
        Optional<Transaction> transaction = transactionRepository.findFirstByTransactionCodeAndIsActiveTrue(transactionCode);
        System.out.println(transaction);
        if(transaction.isPresent()){
            Transaction transaction1 = transaction.get();
            transaction1.setTransactionStatus(ETransactionStatus.valueOf("CANCELLED"));
            transactionRepository.save(transaction1);
        }
    }

    @Override
    public String handleCallbackPayment(Map<String, String> body){
       return paymentGatewayClient.handleCallbackPayament(body);
    }




}


