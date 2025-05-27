package ticket_online.ticket_online.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ticket_online.ticket_online.client.PaymentGatewayClient;
import ticket_online.ticket_online.constant.ETransactionStatus;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.cart.AddCartTicketReqDto;
import ticket_online.ticket_online.dto.transaction.CheckoutReqDto;
import ticket_online.ticket_online.dto.transaction.GetPaymentMethodReqDto;
import ticket_online.ticket_online.model.*;
import ticket_online.ticket_online.repository.*;
import ticket_online.ticket_online.service.PaymentGatewayService;
import ticket_online.ticket_online.service.TransactionService;
import ticket_online.ticket_online.util.GenerateUtil;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {


    // Membuat Logger instance untuk kelas ini
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private PaymentGatewayService paymentGatewayService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PaymentGatewayClient paymentGatewayClient;
//
//    @PostMapping("/checkout")
//    public CompletableFuture<ResponseEntity<ApiResponse<Map<String, Object>>>> checkout(@RequestBody CheckoutReqDto checkoutReqDto){
//        return transactionService.checkout(checkoutReqDto)
//                .thenApply(response -> {
//                   return ResponseEntity.ok(new ApiResponse<>(true, "Transaksi berhasil dibuat", response));
//                })
//                .exceptionally(ex -> {
//                    return ResponseEntity.ok(new ApiResponse<>(false, ex.getMessage(), null));
//                });
//    }

//    @PostMapping("/checkout")
//    public ResponseEntity<ApiResponse<CompletableFuture<Map<String, Object>>>> checkout(@RequestBody CheckoutReqDto checkoutReqDto){
//        try {
//            CompletableFuture<Map<String, Object>>  response = transactionService.checkout(checkoutReqDto);
//            return ResponseEntity.ok(new ApiResponse<>(true, "Event Detail retrieved", response));
//        }catch (RuntimeException e){
//            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
//        }
//    }

    @Value("${paymentgateway.duitku.merchantcode}")
    private String MERCHANT_CODE;

    @Value("${paymentgateway.duitku.apikey}")
    private String API_KEY;

    @Value("${paymentgateway.duitku.callbackurl}")
    private String CALLBACK_URL;

    @Value("${paymentgateway.duitku.returnurl}")
    private String RETURN_URL;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private DetailTransactionRepository detailTransactionRepository;

    @Autowired
    private CategoryTicketRepository categoryTicketRepository;


    @Autowired
    private CartRepository cartRepository;

//    @Async
//    @PostMapping("/checkout")
//    public CompletableFuture<ResponseEntity<Map<String, Object>>> checkoutd(@RequestBody CheckoutReqDto checkoutReqDto) {
//        // Simulasi persiapan parameter untuk API payment gateway
//        Map<String, Object> params = new HashMap<>();
//        params.put("tesmapsss", "sdsd");
//        params.put("tesmap", 20000);
//
//        // Kamu bisa memproses data lain sesuai kebutuhan
//        return processPayment(params, checkoutReqDto);
//    }

    // Fungsi untuk memproses payment asinkron
//    private CompletableFuture<ResponseEntity<Map<String, Object>>> processPayment(Map<String, Object> params, CheckoutReqDto checkoutReqDto) {
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                // Simulasi pemrosesan data dan panggilan API eksternal (misalnya ke payment gateway)
//                System.out.println("Processing payment for: " + checkoutReqDto.getUserId());
//
//                // Simulasi delay atau pemanggilan API eksternal
//                Thread.sleep(3000);  // Simulasi delay, misalnya panggil API eksternal
//
//                // Misalnya jika pemrosesan sukses, kembalikan response
//                Map<String, Object> response = new HashMap<>();
//                response.put("success", true);
//                response.put("transactionId", "TX123456789");
//                response.put("message", "Payment processed successfully");
//
//                return ResponseEntity.ok(response);
//
//            } catch (Exception e) {
//                // Tangani exception jika ada error selama pemrosesan
//                Map<String, Object> errorResponse = new HashMap<>();
//                errorResponse.put("success", false);
//                errorResponse.put("message", "Error while processing payment: " + e.getMessage());
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//            }
//        });
//    }

    //    @Transactional(rollbackFor = Exception.class)
//        @PostMapping("/checkout")
//        public  CompletableFuture<ResponseEntity<Map<String, Object>>> checkoutd(@RequestBody CheckoutReqDto checkoutReqDto){
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("tesmapsss", "sdsd");
//        params.put("tesmap",20000);
//        return CompletableFuture.completedFuture(ResponseEntity.ok(params));
//        }

    @PostMapping("/checkout")
    public  CompletableFuture<ResponseEntity<Map<String, Object>>> checkout(@RequestBody CheckoutReqDto checkoutReqDto){
        String merchantCode = MERCHANT_CODE;
        try {
        System.out.println("[STEP 1] Validasi Event...");
            Optional<Event> getEvent = eventRepository.findFirstBySlugAndIsActiveTrue(checkoutReqDto.getSlug());
            if(getEvent.isEmpty()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event not available");
            }

            List<CheckoutReqDto.Participans> participansPayload = checkoutReqDto.getParticipants();
            List<CheckoutReqDto.DetailCarts> detailCartsPayload = checkoutReqDto.getDetailCartTicket();
            String transactionCode = GenerateUtil.transactionCode();
            Transaction transaction1 = new Transaction();
            transaction1.setTransactionCode(transactionCode);
            transaction1.setEventId(getEvent.get());
            transaction1.setPaymentMethod(checkoutReqDto.getPaymentMethod());
            transactionRepository.save(transaction1);


        System.out.println("[STEP 2] Proses Partisipan...");
            Long primaryVisitorId = null;
            int total_price = 0;
            int total_qty = 0;

            for (int i = 0; i < checkoutReqDto.getParticipants().size(); i++) {
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


        System.out.println("[STEP 3] Hitung Harga...");
            for (int i = 0; i < detailCartsPayload.size(); i++) {
                Optional<CategoryTicket> categoryTicket = categoryTicketRepository.findById(detailCartsPayload.get(i).getCategory_ticket_id());
                if(categoryTicket.isPresent()){
                    CategoryTicket getCategoryTicket = categoryTicket.get();
                    total_qty += detailCartsPayload.get(i).getTotal();
                    total_price += getCategoryTicket.getPrice() * detailCartsPayload.get(i).getTotal();
                }else{
//                    throw new RuntimeException("tiket not available");
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket not available");
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


            System.out.println("[STEP 4] Buat Request ke Payment Gateway...");
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
            params.put("merchantCode", merchantCode);
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

            Optional<Transaction> getTransactionFirst =  transactionRepository.findById(transaction1.getId());

            if(getTransactionFirst.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
            }

                int finalTotal_qty = total_qty;
                int finalTotal_price = total_price;

                return  paymentGatewayClient.transactionRequest(params).thenApply(response -> {
                    try {

                        if (response == null) { //## FIX LOAD DATA NULL FRONTEND
                            throw new CompletionException(new RuntimeException("Response dari payment gateway null"));
                        }

                        System.out.println("[STEP 5] Sukses panggil payment gateway, update transaksi...");
                    Transaction updateTransaction = getTransactionFirst.get();
                    transactionService.finalizeTransaction(
                            updateTransaction,
                            response,
                            finalTotal_qty,
                            finalTotal_price,
                            expiryPeriod,
                            checkoutReqDto.getUserId(),
                            merchantOrderId,
                            transactionCode
                    );


                        response.put("transaction_code", transactionCode);
                        return ResponseEntity.ok(response);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new CompletionException("Gagal menyimpan transaksi ke database", e);
                    }
                }).exceptionally(ex -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("message", ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                    error.put("data", null);
                    System.out.println("[ERROR] Exception saat payment gateway atau finalize: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
                });

        } catch (Exception e) {
            e.printStackTrace();  // Log ke konsol
            System.out.println("errorsdsd" + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            error.put("data", null);
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
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

    @GetMapping("/check-transaction-exists/{userId}/{slug}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> checkUserExcheckIfCurrentTransactionExists(@PathVariable Long userId, @PathVariable String slug){
        try {
            List<Map<String,Object>> response =  transactionService.checkIfCurrentTransactionEventForUserExists(slug, userId);
            return  ResponseEntity.ok(new ApiResponse<>(true, "fsd", response));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{transactionCode}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelledTransaction(@PathVariable String transactionCode){
        try {
            transactionService.cancelledTransaction(transactionCode);
            return  ResponseEntity.ok(new ApiResponse<>(true, "canceled transaction successfully", transactionCode));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/callback")
    public ResponseEntity<ApiResponse<String>> callback(@RequestParam Map<String, String> body){
        try {
           String handleCallback = transactionService.handleCallbackPayment(body);
            return  ResponseEntity.ok(new ApiResponse<>(true, "callback successfully", handleCallback));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }































}
