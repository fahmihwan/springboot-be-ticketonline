package ticket_online.ticket_online.service;

import com.fasterxml.jackson.databind.JsonNode;
import ticket_online.ticket_online.dto.transaction.CheckoutReqDto;
import ticket_online.ticket_online.dto.transaction.TransactionDetailHistoriesDto;
import ticket_online.ticket_online.dto.transaction.TransactionHistoriesDto;
import ticket_online.ticket_online.model.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface TransactionService {

    public CompletableFuture<Map<String,Object>> checkout(CheckoutReqDto checkoutReqDto);

    public List<Map<String, Object>> checkIfCurrentTransactionEventForUserExists(String slug, Long userId);

    public void updateStatusTransactionByCode(String transactionCode);

    public void cancelledTransaction(String transactionCode);

    public String handleCallbackPayment(Map<String, String> body);

    public void finalizeTransaction(
            Transaction transaction,
            Map<String, Object> response,
            Integer finalTotal_qty,
            Integer finalTotal_price,
            Integer expiryPeriod,
            Long userId,
            String merchantOrderId,
            String transactionCode
    );
}
