package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.service.CategoryTicketService;

@RestController
@RequestMapping("/api/cetegory-ticket")
public class CategoryTicketController {

    @Autowired
    CategoryTicketService categoryTicketService;
//
//    @GetMapping("/{eventId}")
////    public ResponseEntity<ApiResponse<>>

    @PostMapping
    public ApiResponse<CategoryTicket> createCategoryAdmin(@RequestBody CategoryTicket categoryTicket){
        CategoryTicket response = categoryTicketService.createCategoryTicket(categoryTicket);
        return ApiResponse.<CategoryTicket>builder().data(response).build();
    }

}
