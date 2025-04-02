package ticket_online.ticket_online.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticket_online.ticket_online.dto.cart.AddCartTicketReqDto;
import ticket_online.ticket_online.model.Cart;
import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.repository.*;
import ticket_online.ticket_online.service.CartService;

import java.util.Optional;

@Slf4j
@Service
public class CartServiceImpl implements CartService {


    @Autowired
    private CategoryTicketRepository categoryTicketRepository;

    @Autowired
    private CartRepository cartRepository;


    public AddCartTicketReqDto createCartTicket(AddCartTicketReqDto addCartTicketReqDto){
        try {

            for (int i = 0; i < addCartTicketReqDto.getDetailTransactions().size(); i++) {
                AddCartTicketReqDto.DetailTicketReqDto reqCategoryTicket = addCartTicketReqDto.getDetailTransactions().get(i);

                Optional<CategoryTicket> categoryTicket = categoryTicketRepository.findFirstByIdAndIsActiveTrue(reqCategoryTicket.getCategoryTicketId());

                if(categoryTicket.isEmpty()){
                    throw new RuntimeException("ticket is not Exists");
                }


                if(categoryTicket.get().getQuotaTicket() >= reqCategoryTicket.getTotal()) {
                    Integer qty = categoryTicket.get().getQuotaTicket() - reqCategoryTicket.getTotal();

                    CategoryTicket categoryTicket1 = categoryTicket.get();
                    categoryTicket1.setQuotaTicket(qty);
                    categoryTicketRepository.saveAndFlush(categoryTicket1);

                    Cart cart = new Cart();
                    cart.setCategoryTicketid(categoryTicket1.getId());
                    cart.setUserId(1L);
                    cart.setTotal(reqCategoryTicket.getTotal());
                    cartRepository.save(cart);
                }
            }


            return addCartTicketReqDto;

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }

    }



}
