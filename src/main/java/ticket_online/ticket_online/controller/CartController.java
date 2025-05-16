package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.cart.AddCartTicketReqDto;
import ticket_online.ticket_online.dto.cart.CartResDto;
import ticket_online.ticket_online.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}/{slug}")
    public  ResponseEntity<ApiResponse<List<CartResDto>>> findCartUser(@PathVariable Long userId, @PathVariable String slug){
        try {
           List<CartResDto> cartUser = cartService.findCartUser(userId, slug);
            System.out.println(cartUser);
            return  ResponseEntity.ok(new ApiResponse<>(true, "list cart user", cartUser));

        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }

    }

    @PostMapping("/cart-ticket")
    public ResponseEntity<ApiResponse<AddCartTicketReqDto>> storeDetailTransaction(@RequestBody AddCartTicketReqDto addCartTicketReqDto){
        try {
            AddCartTicketReqDto response =  cartService.createCartTicket(addCartTicketReqDto, addCartTicketReqDto.getUserId());
            return ResponseEntity.ok(new ApiResponse<>(true, "cart created successfully", response));
        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

}
