package ticket_online.ticket_online.service;

import ticket_online.ticket_online.dto.transaction.CheckoutReqDto;
import ticket_online.ticket_online.dto.transaction.TransactionDetailHistoriesDto;
import ticket_online.ticket_online.dto.transaction.TransactionHistoriesDto;
import ticket_online.ticket_online.model.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TransactionService {

    public Map<String,Object> checkout(CheckoutReqDto checkoutReqDto);

    public List<Map<String, Object>> checkIfCurrentTransactionEventForUserExists(String slug, Long userId);


    public List<TransactionHistoriesDto> transactionHistories(Long userId);


    public TransactionDetailHistoriesDto transactionDetailHistories(String transactionCode);

    public void cancelledTransaction(String transactionCode);


}
