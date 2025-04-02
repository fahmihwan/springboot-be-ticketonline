package ticket_online.ticket_online.service;

import ticket_online.ticket_online.dto.cart.AddCartTicketReqDto;

public interface CartService {

    public AddCartTicketReqDto createCartTicket(AddCartTicketReqDto addCartTicketReqDto);
}
