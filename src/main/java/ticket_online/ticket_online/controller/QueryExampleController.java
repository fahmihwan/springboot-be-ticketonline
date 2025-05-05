package ticket_online.ticket_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.Transaction;
import ticket_online.ticket_online.model.User;
import ticket_online.ticket_online.repository.EventRepository;
import ticket_online.ticket_online.service.QueryExampleService;
import ticket_online.ticket_online.service.impl.QueryExampleServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/example")
public class QueryExampleController {

    @Autowired
    private QueryExampleService queryExampleService;

    @Autowired
    private QueryExampleServiceImpl queryExampleServiceImpl;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/api-jsonplaceholder")
    public ResponseEntity<String> getApiJsonPlaceHolder(){
        return  ResponseEntity.ok(queryExampleServiceImpl.getApiJSONPlaceHolder());
    }

    @GetMapping("/get-all-event-repo")
    public ApiResponse<List<EventHomeResDto>> getAllEvents(){
        List<EventHomeResDto> response = queryExampleService.getAllEventUseRepo();
        return  ApiResponse.<List<EventHomeResDto>>builder().data(response).build();
    }

    @GetMapping("/get-all-event-jdbc")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAllEventsJDBC(){
        ApiResponse<List<Map<String, Object>>> response = queryExampleService.getAllEventUseJDBC();
        if(response.getSuccess()){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/get-all-event-em")
    public ApiResponse<List<Map<String, Object>>> getAllEventsJEM(){
        List<Map<String, Object>> response = queryExampleService.getAllEventUseEM();
        return  ApiResponse.<List<Map<String, Object>>>builder().data(response).build();
    }

    @GetMapping("/get-user/{id}")
    public ApiResponse<User> findUserById(@PathVariable Long id){
        User response = queryExampleService.findByUserId(id);
        return ApiResponse.<User>builder().data(response).build();
    }

    @GetMapping("/event-findbyid-custome/{id}")
    public ApiResponse<Event> findEventByIdCustome(@PathVariable Long id){
        Event response = queryExampleService.findByIdJPA(id);
        return ApiResponse.<Event>builder().data(response).build();
    }

    @GetMapping("/event-findbyid-jpa/{id}")
    public ApiResponse<Event> findEventIdJPA(@PathVariable Long id){;
        Event response= queryExampleService.findByIdCustome(id);
        return ApiResponse.<Event>builder().data(response).build();
    }

    @GetMapping("/event-findbyid-jpa-object/{id}")
    public ApiResponse<EventResDto> findEventIdJPAObject(@PathVariable Long id){;
        EventResDto response= queryExampleService.findByIdJpaModelObject(id);
        return ApiResponse.<EventResDto>builder().data(response).build();
    }

    @GetMapping("/event-pagination-repo")
    public ResponseEntity<Page<Event>> getPaginationRepository(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Page<Event> response = queryExampleService.getPaginationRepository(page, size);
        return  ResponseEntity.ok(response);
    }

    @GetMapping("/event-pagination-jdbc")
    public ResponseEntity<ApiResponse<List<EventHomeResDto>>> getPaginationJdbc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        ApiResponse<List<EventHomeResDto>> response = queryExampleService.getPaginationJdbc(page, size);
        return  ResponseEntity.ok(response);
    }

    @GetMapping("/get-only-event-repo")
    public ResponseEntity<List<Event>> getOnlyEventRepo(){
        return  ResponseEntity.ok(queryExampleService.getEvents());
    }





    @GetMapping("/find-all-by-transactioncode")
    public ResponseEntity<ApiResponse<Optional<Transaction>>> getByTransactionCode(){

        try {
            Optional<Transaction> transaction = queryExampleService.findAllByTransactionCode();
            return ResponseEntity.ok(new ApiResponse<>(true,"query", transaction));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        }
    }






    // findByIdJpaModelObject




}
