package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.dto.WebResponse;
import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.service.EventService;
import ticket_online.ticket_online.service.QueryExampleService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/event")
public class EventController {


    @Autowired
    EventService eventService;

    @GetMapping
    public WebResponse<List<Event>> getEventWithCategories(){
        List<Event> response =  eventService.getEventWithCategories();
        return WebResponse.<List<Event>>builder().data(response).build();
    }

    @PostMapping
    public  WebResponse<Event> createEventAdmin(@RequestBody Event event){
        Event response = eventService.createEventAdmins(event);
        return WebResponse.<Event>builder().data(response).build();
    }

    @DeleteMapping("/remove/{id}")
    public WebResponse<Boolean> removeEventAdmin(@PathVariable Long id){
       Boolean response =  eventService.removeEventAdmin(id);
        return WebResponse.<Boolean>builder().data(response).build();
    }

    @DeleteMapping("/destroy/{id}")
    public WebResponse<Boolean> destroyEventAdminWithTickets(@PathVariable Long id){
        Boolean response =  eventService.destroyEventAdminWithTickets(id);
        return WebResponse.<Boolean>builder().data(response).build();
    }

}
