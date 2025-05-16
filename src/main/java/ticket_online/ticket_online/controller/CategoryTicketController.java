package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.categoryTicket.CategoryTicketReqDto;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.service.CategoryTicketService;

import java.util.List;

@RestController
@RequestMapping("/api/cetegory-ticket")
public class CategoryTicketController {

    @Autowired
    CategoryTicketService categoryTicketService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryTicket>> createCategoryAdmin(@RequestBody CategoryTicketReqDto categoryTicketReqDto){
        try {
            CategoryTicket response = categoryTicketService.createCategoryTicket(categoryTicketReqDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Category Ticket retrieved successfully", response));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remove/{categoryTicketId}")
    public ResponseEntity<ApiResponse<Boolean>> removeCatgoryTicketAdmin(@PathVariable Long categoryTicketId){
//        return  null;
        try {
            Boolean response = categoryTicketService.removeCategoryTicketById(categoryTicketId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event retrieved successfully", response));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
