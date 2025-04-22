package ticket_online.ticket_online.service;

import ticket_online.ticket_online.dto.cart.AddCartTicketReqDto;
import ticket_online.ticket_online.dto.cart.CartResDto;

import java.util.List;

public interface CartService {

    public List<CartResDto> findCartUser(Long userId);
    public AddCartTicketReqDto createCartTicket(AddCartTicketReqDto addCartTicketReqDto);
    public Boolean destroyCartByUserId(Long id);
}
