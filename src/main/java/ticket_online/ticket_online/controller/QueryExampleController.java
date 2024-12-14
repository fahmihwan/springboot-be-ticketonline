package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticket_online.ticket_online.dto.WebResponse;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.User;
import ticket_online.ticket_online.service.QueryExampleService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/example")
public class QueryExampleController {

    @Autowired
    private QueryExampleService queryExampleService;

    @GetMapping("/get-all-event-repo")
    public WebResponse<List<EventHomeResDto>> getAllEvents(){
        List<EventHomeResDto> response = queryExampleService.getAllEventUseRepo();
        return  WebResponse.<List<EventHomeResDto>>builder().data(response).build();
    }

    @GetMapping("/get-all-event-jdbc")
    public WebResponse<List<Map<String, Object>>> getAllEventsJDBC(){
        List<Map<String, Object>> response = queryExampleService.getAllEventUseJDBC();
        return  WebResponse.<List<Map<String, Object>>>builder().data(response).build();
    }

    @GetMapping("/get-all-event-em")
    public WebResponse<List<Map<String, Object>>> getAllEventsJEM(){
        List<Map<String, Object>> response = queryExampleService.getAllEventUseEM();
        return  WebResponse.<List<Map<String, Object>>>builder().data(response).build();
    }

    @GetMapping("/get-user/{id}")
    public WebResponse<User> findUserById(@PathVariable Long id){
        User response = queryExampleService.findByUserId(id);
        return WebResponse.<User>builder().data(response).build();
    }

    @GetMapping("/event-findbyid-custome/{id}")
    public WebResponse<Event> findEventByIdCustome(@PathVariable Long id){
        Event response = queryExampleService.findByIdJPA(id);
        return WebResponse.<Event>builder().data(response).build();
    }

    @GetMapping("/event-findbyid-jpa/{id}")
    public WebResponse<Event> findEventIdJPA(@PathVariable Long id){;
        Event response= queryExampleService.findByIdCustome(id);
        return WebResponse.<Event>builder().data(response).build();
    }

    @GetMapping("/event-findbyid-jpa-object/{id}")
    public WebResponse<EventResDto> findEventIdJPAObject(@PathVariable Long id){;
        EventResDto response= queryExampleService.findByIdJpaModelObject(id);
        return WebResponse.<EventResDto>builder().data(response).build();
    }

    // findByIdJpaModelObject




}
