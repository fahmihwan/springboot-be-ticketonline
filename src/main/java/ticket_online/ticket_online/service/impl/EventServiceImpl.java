package ticket_online.ticket_online.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Parameter;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.repository.EventRepository;
import ticket_online.ticket_online.service.CategoryTicketService;
import ticket_online.ticket_online.service.EventService;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    CategoryTicketService categoryTicketService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    // ORM TABLE
    public List<Event> getEventWithCategories(){
        return eventRepository.findAll();
    }

    // Repository custome
    public List<EventResDto> getAllEvent(){
        return  null;
    }

    public List<Map<String, Object>> getAllEventUseJDBC(){
        String sql = "SELECT ev.id, ev.event_title FROM events ev";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getAllEventUseEM(){
        String sql = "SELECT ev.id, ev.event_title FROM events ev";
       Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }


    public Event getEventById(Long id){
        return eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public Event createEventAdmins(Event event){

        return eventRepository.save(event);
    }

    public Boolean removeEventAdmin(Long id){
        try {
            Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
            //  cek lagi jika event sudah ada transaksi maka tidak boleh di hapus
            event.setIs_active(false);
            eventRepository.save(event);
            return true;
        }catch (RuntimeException e){
            System.out.println("Error: " + e.getMessage());
            return  false;
        }
    }

    @Transactional
    public Boolean destroyEventAdminWithTickets(Long eventId){
        try {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
            //            cek lagi jika event sudah ada transaksi maka tidak boleh di hapus
            eventRepository.deleteById(eventId);
            categoryTicketService.destroyCategoryTicketByEventId(eventId);
            return  true;
        }catch (RuntimeException e){
            System.out.println("Error: " + e.getMessage());
            return  false;
        }

    }


}

