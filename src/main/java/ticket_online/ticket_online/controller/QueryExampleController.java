package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticket_online.ticket_online.dto.WebResponse;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.service.QueryExampleService;

import java.util.List;
import java.util.Map;

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
}
