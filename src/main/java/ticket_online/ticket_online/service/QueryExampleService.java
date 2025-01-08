package ticket_online.ticket_online.service;

import org.springframework.data.domain.Page;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.User;

import java.util.List;
import java.util.Map;

public interface QueryExampleService {

    //pakai ORM
    public List<Event> getEventWithCategories();

    //pakai DTO
    public List<EventHomeResDto> getAllEventUseRepo();

    //query raw pakai JDBC easy to use
    public ApiResponse<List<Map<String, Object>>> getAllEventUseJDBC();

    //pakai entityManeger (harus di looing ulang, atau di map, males.)
    public List<Map<String, Object>> getAllEventUseEM();

    public User findByUserId(Long id);

    public Event findByIdCustome(Long id);

    public Event findByIdJPA(Long id);

    public EventResDto findByIdJpaModelObject(Long id);

    public Page<Event> getPaginationRepository(int page, int size);

    public ApiResponse<List<EventHomeResDto>> getPaginationJdbc(int page, int size);


    public List<Event> getEvents();



}
