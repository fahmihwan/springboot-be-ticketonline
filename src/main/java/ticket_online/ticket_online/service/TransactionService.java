package ticket_online.ticket_online.service;

import ticket_online.ticket_online.dto.transaction.AddCartTicketReqDto;
import ticket_online.ticket_online.model.DetailTransaction;
import ticket_online.ticket_online.model.Transaction;

public interface TransactionService {

    public DetailTransaction createCartTicket(AddCartTicketReqDto addCartTicketReqDto);



}
