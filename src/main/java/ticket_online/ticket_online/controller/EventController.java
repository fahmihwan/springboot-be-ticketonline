package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.event.EventDetailResDto;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.service.EventService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping("/{total}/events")
    public ResponseEntity<ApiResponse<List<EventHomeResDto>>> getEventHome(@PathVariable Integer total){
         ApiResponse<List<EventHomeResDto>> response =  eventService.getEventWithMinPrice(total);
        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventDetailResDto>> getEventDetail(@PathVariable Long id){
        ApiResponse<EventDetailResDto> response = eventService.getEventById(id);
        if(response.getSuccess()){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/{eventId}/with-category-tickets")
    public ResponseEntity<ApiResponse<Event>> getEventWithAllCategoryTickets(@PathVariable Long eventId){
        ApiResponse<Event> response = eventService.getEventWithAllCategoryTickets(eventId);
        if(response.getSuccess()){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @PostMapping
//    public ResponseEntity<ApiResponse<Event>> createEventAdmin(@RequestBody Event event){
    public ResponseEntity<ApiResponse<Event>> createEventAdmin(@RequestBody Event event, @RequestParam("image") MultipartFile image){
        System.out.println(event);
        ApiResponse<Event> response = eventService.createEventAdmins(event, image);
          if(response.getSuccess()){
              return ResponseEntity.status(HttpStatus.CREATED).body(response);
          }else{
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
          }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ApiResponse<Boolean>> removeEventAdmin(@PathVariable Long id){
        ApiResponse<Boolean> response = eventService.removeEventAdmin(id);
        if(response.getSuccess()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.status(HttpStatus. NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/destroy/{id}")
    public ApiResponse<Boolean> destroyEventAdminWithTickets(@PathVariable Long id){
        Boolean response =  eventService.destroyEventAdminWithTickets(id);
        return ApiResponse.<Boolean>builder().data(response).build();
    }

}
