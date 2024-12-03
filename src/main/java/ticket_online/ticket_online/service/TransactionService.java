package ticket_online.ticket_online.service;

import ticket_online.ticket_online.dto.transaction.CheckoutReqDto;
import ticket_online.ticket_online.model.Transaction;

public interface TransactionService {

    public Transaction checkoutTicket(CheckoutReqDto checkoutReqDto);

}
