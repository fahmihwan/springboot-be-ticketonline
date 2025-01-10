package ticket_online.ticket_online.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.event.EventDetailResDto;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
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
        try {
            List<EventHomeResDto> response =  eventService.getEventWithMinPrice(total);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event retrieved successfully", response));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }

    }

    @GetMapping("/admin/pagination")
    public ResponseEntity<ApiResponse<Page<Event>>> getEventAdmin(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                  @RequestParam(value = "size",defaultValue = "5") int size){

        try {
            Page<Event> response = eventService.getEventPagination(page, size);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event retrieved successfully", response));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<EventDetailResDto>> getEventDetailBySlug(@PathVariable String slug){
        try {
            EventDetailResDto response = eventService.getEventBySlug(slug);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event Detail retrieved", response));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{eventId}/with-category-tickets")
    public ResponseEntity<ApiResponse<Event>> getEventWithAllCategoryTickets(@PathVariable String slug){
        try {
            Event response = eventService.getEventWithAllCategoryTickets(slug);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event Detail retrieved", response));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @PostMapping
    public ResponseEntity<ApiResponse<Event>> storeEventAdmin(@ModelAttribute EventReqDto eventReqDto){

        try {
            Event response = eventService.createEventAdmin(eventReqDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event has Created", response));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }

    }

    @PutMapping("/{slug}")
    public ResponseEntity<ApiResponse<Event>> updateEventAdmin(@ModelAttribute EventReqDto eventReqDto, @PathVariable String slug){

        try {


            Event response = eventService.updateEventAdmin(eventReqDto, slug);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Event has updated ", response));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }

    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ApiResponse<Boolean>> removeEventAdmin(@PathVariable Long id){

        try {
            Boolean response = eventService.removeEventAdmin(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event has deleted", response));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }

    }

    @DeleteMapping("/destroy/{id}")
    public ResponseEntity<ApiResponse<Boolean>> destroyEventAdminWithTickets(@PathVariable Long id){
        try {
            Boolean response =  eventService.destroyEventAdminWithTickets(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Event has deleted", response));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }

    }

    ////=================================================================================================================================================================
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<EventDetailResDto>> getEventDetailById(@PathVariable Long id){
//        ApiResponse<EventDetailResDto> response = eventService.getEventById(id);
//        if(response.getSuccess()){
//            return ResponseEntity.ok(response);
//        }else{
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//    }
}
