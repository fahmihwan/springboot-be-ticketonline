package ticket_online.ticket_online.service;

import ticket_online.ticket_online.dto.cart.AddCartTicketReqDto;
import ticket_online.ticket_online.dto.transaction.CheckoutReqDto;

import java.util.Map;

public interface TransactionService {

    public Map<String,Object> checkout(CheckoutReqDto checkoutReqDto);

}
