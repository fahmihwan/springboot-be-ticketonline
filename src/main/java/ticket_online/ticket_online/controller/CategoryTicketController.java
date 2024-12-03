package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticket_online.ticket_online.dto.WebResponse;
import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.service.CategoryTicketService;

@RestController
@RequestMapping("/api/cetegory-ticket")
public class CategoryTicketController {

    @Autowired
    CategoryTicketService categoryTicketService;

    @PostMapping
    public WebResponse<CategoryTicket> createCategoryAdmin(@RequestBody CategoryTicket categoryTicket){
        CategoryTicket response = categoryTicketService.createCategoryTicket(categoryTicket);
        return WebResponse.<CategoryTicket>builder().data(response).build();
    }
}
