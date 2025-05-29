package ticket_online.ticket_online.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.event.EventDetailResDto;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.dto.event.EventLineUpResDto;
import ticket_online.ticket_online.dto.event.EventReqDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping("/{total}/events")
    public ResponseEntity<ApiResponse<List<EventHomeResDto>>> getEventHome(@PathVariable Integer total){
//        System.out.println("ssksk");
        try {
            List<EventHomeResDto> response =  eventService.getEventWithMinPrice(total);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event retrieved successfully ", response));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }

    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/admin/pagination")
    public ResponseEntity<ApiResponse<Page<EventLineUpResDto>>> getEventAdmin(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                              @RequestParam(value = "size",defaultValue = "5") int size){

        try {
            Page<EventLineUpResDto> response = eventService.getEventPagination(page, size);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event retrieved successfully", response));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<EventDetailResDto>> getEventDetailBySlug(@PathVariable String slug){
//        System.out.println("wkwkwkwk "+ slug );
        try {
            EventDetailResDto response = eventService.getEventBySlug(slug);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event Detail retrieved", response));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{slug}/with-category-tickets")
    public ResponseEntity<ApiResponse<Event>> getEventWithAllCategoryTickets(@PathVariable String slug){
        try {
            Event response = eventService.getEventWithAllCategoryTickets(slug);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event Detail retrieved", response));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<Event>> storeEventAdmin(@ModelAttribute EventReqDto eventReqDto,
                                                              @RequestParam(name = "image", required = false) MultipartFile image){

        try {
            Event response = eventService.createEventAdmin(eventReqDto, image);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event has Created", response));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }

    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{slug}")
    public ResponseEntity<ApiResponse<Event>> updateEventAdmin(@ModelAttribute EventReqDto eventReqDto,
                                                               @RequestParam(name = "image", required = false) MultipartFile image,
                                                               @PathVariable String slug){

        try{
            System.out.println(image);
            Event response = eventService.updateEventAdmin(eventReqDto, slug, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Event has updated ", response));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ApiResponse<Boolean>> removeEventAdmin(@PathVariable Long id){

        try {
            Boolean response = eventService.removeEventAdmin(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event has deleted", response));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/destroy/{id}")
    public ResponseEntity<ApiResponse<Boolean>> destroyEventAdminWithTickets(@PathVariable Long id){
        try {
            Boolean response =  eventService.destroyEventAdminWithTickets(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event has deleted", response));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }

    }

}
