package ticket_online.ticket_online.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import ticket_online.ticket_online.client.PaymentGatewayClient;
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
            String callbackUrl = "http://example.com/callback"; // url untuk callback
            String returnUrl = "http://example.com/return"; // url untuk redirect
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
                        updateTransaction.setTransactionStatus(Transaction.TransactionStatus.valueOf("PENDING"));
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

    //    HISTORIES
    @Override
    public List<TransactionHistoriesDto> transactionHistories(Long userId){


        String sql = "select t.transaction_code, e.image as image, t.transaction_status, e.event_title, t.created_at as tgl_transaksi, t.total_price, t.expiry_period  from transactions t\n" +
                "        inner join detail_transactions dt on t.id = dt.transaction_id\n" +
                "        inner join category_tickets ct on ct.id  = dt.category_ticket_id\n" +
                "        inner join events e on e.id = ct.event_id\n" +
                "        where t.user_id  = ?\n" +
                "        group by t.transaction_code, e.event_title, t.created_at, t.total_price, t.transaction_status, e.image, t.expiry_period ORDER BY t.created_at DESC";

        List<TransactionHistoriesDto>  transactionHistoriesDtos =  jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TransactionHistoriesDto.class), userId);
        for (TransactionHistoriesDto dto : transactionHistoriesDtos){


            Object value = dto.getExpiry_period();
            boolean isExpired = false;
            if (value instanceof Number) {
                Integer expiryPeriod = ((Number) value).intValue(); // ✔️ aman dikonversi
                Timestamp createdAtTimestamp = Timestamp.valueOf(dto.getTgl_transaksi());
                 isExpired = CheckUtil.checkIsExpiredTransaction(expiryPeriod, createdAtTimestamp);
            }

            if(isExpired && dto.getTransaction_status().equals("PENDING")){
                String updateSql = "UPDATE transactions SET transaction_status = 'EXPIRED' WHERE transaction_code = ?";
                jdbcTemplate.update(updateSql, dto.getTransaction_code());
            }

            dto.setImage(GenerateUtil.generateImgUrl((String) dto.getImage()));
        }
        return transactionHistoriesDtos;
    }

    //    HISTORIES
    @Override
    public TransactionDetailHistoriesDto transactionDetailHistories(String transactionCode) {

        updateStatusTransactionByCode(transactionCode);

        String sql = "select t.id, t.transaction_code, t.pg_payment_url as payment_url, t.pg_va_number as virtual_account, e.image as img, t.event_id, t.user_id, t.transaction_status, t.created_at, " +
                " t.total_qty as total_ticket, t.payment_method, t.pg_payment_amount as total_price\n" +
                " from transactions t\n" +
                " inner join events e on e.id  = t.event_id\n" +
                " where t.transaction_code  = ? limit 1";
        List<TransactionDetailHistoriesDto> transaction =  jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TransactionDetailHistoriesDto.class), transactionCode);

        Optional<User> user = null;
        Optional<Event> event = null;
//
        TransactionDetailHistoriesDto transactionDetailHistoriesDto = new TransactionDetailHistoriesDto();

        if (!transaction.isEmpty()) {
            user = userRepository.findById(transaction.get(0).getUser_id());
            event = eventRepository.findFirstByIdAndIsActiveTrue(transaction.get(0).getEvent_id());

            transactionDetailHistoriesDto.setTransaction_code(transaction.get(0).getTransaction_code());
            transactionDetailHistoriesDto.setTransaction_status(String.valueOf(transaction.get(0).getTransaction_status()));
            LocalDateTime created_at = ConvertUtil.convertToLocalDateTime(transaction.get(0).getCreatedAt());
            transactionDetailHistoriesDto.setTransction_date(created_at);
            transactionDetailHistoriesDto.setPayment_url(transaction.get(0).getPayment_url());
            transactionDetailHistoriesDto.setVirtual_account(transaction.get(0).getVirtual_account());
            transactionDetailHistoriesDto.setTotal_ticket(transaction.get(0).getTotal_ticket());
            transactionDetailHistoriesDto.setTotal_price(transaction.get(0).getTotal_price());
            transactionDetailHistoriesDto.setPayment_method(transaction.get(0).getPayment_method());
            transactionDetailHistoriesDto.setImg(GenerateUtil.generateImgUrl(transaction.get(0).getImg()));

            if (user.isPresent()){
                User mapUser = new User();
                mapUser.setFullName(user.get().getFullName());
                mapUser.setEmail(user.get().getEmail());
                transactionDetailHistoriesDto.setUser(mapUser);
            }

            if(event.isPresent()){
                Event mapEvent = new Event();
                mapEvent.setEvent_title(event.get().getEvent_title());
                LocalDateTime dateSchedule = LocalDateTime.parse(event.get().getSchedule().toString());
                mapEvent.setSchedule(dateSchedule);
                mapEvent.setImage(GenerateUtil.generateImgUrl(event.get().getImage()));
                mapEvent.setVenue(event.get().getVenue());
                mapEvent.setSlug(event.get().getSlug());
                transactionDetailHistoriesDto.setEvent(mapEvent);
            }

        }

        List<Map<String, Object>> getVisitor = transactionRepository.findVisitorFromTransaction(transactionCode);
        List<TransactionDetailHistoriesDto.Participans> participans = new ArrayList<>();
        for (int i = 0; i < getVisitor.size(); i++) {
            TransactionDetailHistoriesDto.Participans participan = new TransactionDetailHistoriesDto.Participans();
            participan.setAddress((String) getVisitor.get(i).get("address"));
            participan.setCategory_name((String) getVisitor.get(i).get("category_name"));

            LocalDate birthDate = LocalDate.parse(getVisitor.get(i).get("birth_date").toString()); // Spring Boot akan mengonversi ISO 8601 string menjadi LocalDateTime
            participan.setBirth_date(birthDate);


            participan.setEmail((String) getVisitor.get(i).get("email"));
            participan.setFull_name((String) getVisitor.get(i).get("full_name"));
            participan.setGender((String) getVisitor.get(i).get("gender"));
            participan.setIncrement_id((Integer) getVisitor.get(i).get("increment_id"));
            participan.setTelp((String) getVisitor.get(i).get("phone_number"));
            participans.add(participan);
        }
        transactionDetailHistoriesDto.setParticipansList(participans);

        return transactionDetailHistoriesDto;

    }


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


    public void cancelledTransaction(String transactionCode){
        Optional<Transaction> transaction = transactionRepository.findFirstByTransactionCodeAndIsActiveTrue(transactionCode);
        System.out.println(transaction);
        if(transaction.isPresent()){
            Transaction transaction1 = transaction.get();
            transaction1.setTransactionStatus(Transaction.TransactionStatus.valueOf("CANCELLED"));
            transactionRepository.save(transaction1);
        }

    }



}


