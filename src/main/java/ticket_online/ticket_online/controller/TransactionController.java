package ticket_online.ticket_online.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticket_online.ticket_online.dto.transaction.CheckoutReqDto;
import ticket_online.ticket_online.model.Visitor;

@RestController
@RequestMapping("/api/transaction/checkout")
public class TransactionController {

    @PostMapping
    public void checkout(@RequestBody CheckoutReqDto checkoutReqDto){
//        System.out.println(checkoutReqDto);

//        for ( Visitor visitor : checkoutReqDto.getVisitors()){
//            System.out.println(visitor.getEmail());
//        }
    }
}
