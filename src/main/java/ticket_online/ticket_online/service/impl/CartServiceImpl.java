package ticket_online.ticket_online.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticket_online.ticket_online.dto.cart.AddCartTicketReqDto;
import ticket_online.ticket_online.dto.cart.CartResDto;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.model.Cart;
import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.repository.*;
import ticket_online.ticket_online.service.CartService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CartServiceImpl implements CartService {


    @Autowired
    private CategoryTicketRepository categoryTicketRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<CartResDto> findCartUser(Long userId, String slug){
        String sql = "select c.id, ct.category_name, ct.price,c.total, c.category_ticket_id from carts c\n" +
                "inner join category_tickets ct on ct.id = c.category_ticket_id \n" +
                "inner join events e on e.id  = ct.event_id \n"+
                "where c.user_id = ? and c.is_active=true and e.slug = ? ";
        List<CartResDto> data = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CartResDto.class), userId,slug);
//        System.out.println(data);
        return  data;
    }


    @Transactional
    @Override
    public AddCartTicketReqDto createCartTicket(AddCartTicketReqDto addCartTicketReqDto, Long userId){
        try {

            List<Cart> havingCart = cartRepository.findByUserId(userId);
            if(havingCart.size() > 0){
                cartRepository.deleteByUserId(userId);
            }

            for (int i = 0; i < addCartTicketReqDto.getDetailTransactions().size(); i++) {
                AddCartTicketReqDto.DetailTicketReqDto reqCategoryTicket = addCartTicketReqDto.getDetailTransactions().get(i);

                Optional<CategoryTicket> categoryTicket = categoryTicketRepository.findFirstByIdAndIsActiveTrue(reqCategoryTicket.getCategoryTicketId());

                if(categoryTicket.isEmpty()){
                    throw new RuntimeException("ticket is not Exists");
                }

                if(categoryTicket.get().getQuotaTicket() >= reqCategoryTicket.getTotal()) {
                    CategoryTicket categoryTicket1 = categoryTicket.get();
                    Cart cart = new Cart();
                    cart.setCategoryTicketid(categoryTicket1.getId());
                    cart.setUserId(userId);
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

    @Override
    public Boolean destroyCartByUserId(Long userId){
        try {
            cartRepository.deleteByUserId(userId);
            return  true;
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
