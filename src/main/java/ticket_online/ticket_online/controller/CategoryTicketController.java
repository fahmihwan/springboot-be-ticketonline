package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.categoryTicket.CategoryTicketReqDto;
import ticket_online.ticket_online.dto.categoryTicket.CategoryTicketResDto;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.dto.event.EventLineUpResDto;
import ticket_online.ticket_online.dto.event.EventTicketResDto;
import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.service.CategoryTicketService;
import ticket_online.ticket_online.service.EventService;

import java.util.List;

@RestController
    @RequestMapping("/api/cetegory-ticket")
public class CategoryTicketController {

    @Autowired
    private CategoryTicketService categoryTicketService;

    @Autowired
    private EventService eventService;

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<List<CategoryTicketResDto>>> getListCategoryTicketFromSlug(@PathVariable String slug){
        try {
            List<CategoryTicketResDto> response = categoryTicketService.getListCategoryTicketFromSlug(slug);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event retrieved successfully", response));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/admin/pagination")
    public ResponseEntity<ApiResponse<Page<EventTicketResDto>>> getEventAdmin(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                              @RequestParam(value = "size",defaultValue = "5") int size){

        try {
            Page<EventTicketResDto> response = categoryTicketService.getEventTicketPagination(page, size);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event retrieved successfully", response));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


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
