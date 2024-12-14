package ticket_online.ticket_online.service;

import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface QueryExampleService {

    //pakai ORM
    public List<Event> getEventWithCategories();

    //pakai DTO
    public List<EventHomeResDto> getAllEventUseRepo();

    //query raw pakai JDBC easy to use
    public List<Map<String, Object>> getAllEventUseJDBC();

    //pakai entityManeger (harus di looing ulang, atau di map, males.)
    public List<Map<String, Object>> getAllEventUseEM();

    public User findByUserId(Long id);

    public Event findByIdCustome(Long id);

    public Event findByIdJPA(Long id);

    public EventResDto findByIdJpaModelObject(Long id);
}
