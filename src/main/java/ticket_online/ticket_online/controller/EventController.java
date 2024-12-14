package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.dto.WebResponse;
import ticket_online.ticket_online.dto.event.EventDetailResDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.service.EventService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping("/{total}/fetch")
    public ResponseEntity<WebResponse<List<Map<String, Object>>>> getEventHome(@PathVariable Integer total){
         WebResponse<List<Map<String, Object>>> response =  eventService.getEventWithMinPrice(total);
        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebResponse<EventDetailResDto>> getEventDetail(@PathVariable Long id){
        WebResponse<EventDetailResDto> response = eventService.getEventById(id);
        if(response.getSuccess()){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/{eventId}/with-category-tickets")
    public ResponseEntity<WebResponse<Event>> getEventWithAllCategoryTickets(@PathVariable Long eventId){
        WebResponse<Event> response = eventService.getEventWithAllCategoryTickets(eventId);
        if(response.getSuccess()){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @PostMapping
    public ResponseEntity<WebResponse<Event>> createEventAdmin(@RequestBody Event event){
        WebResponse<Event> response = eventService.createEventAdmins(event);
          if(response.getSuccess()){
              return ResponseEntity.status(HttpStatus.CREATED).body(response);
          }else{
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
          }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<WebResponse<Boolean>> removeEventAdmin(@PathVariable Long id){
        WebResponse<Boolean> response = eventService.removeEventAdmin(id);
        if(response.getSuccess()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.status(HttpStatus. NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/destroy/{id}")
    public WebResponse<Boolean> destroyEventAdminWithTickets(@PathVariable Long id){
        Boolean response =  eventService.destroyEventAdminWithTickets(id);
        return WebResponse.<Boolean>builder().data(response).build();
    }

}
