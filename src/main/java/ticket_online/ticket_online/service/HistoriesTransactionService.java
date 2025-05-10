package ticket_online.ticket_online.service;

import ticket_online.ticket_online.dto.transaction.TransactionDetailHistoriesDto;
import ticket_online.ticket_online.dto.transaction.TransactionHistoriesDto;

import java.util.List;
import java.util.Map;

public interface HistoriesTransactionService {

    public List<TransactionHistoriesDto> transactionHistories(Long userId);

    public TransactionDetailHistoriesDto transactionDetailHistories(String transactionCode);

    public Map<String, Object> allOfTransactionFromUsersAdmin(int page, int size);

}
