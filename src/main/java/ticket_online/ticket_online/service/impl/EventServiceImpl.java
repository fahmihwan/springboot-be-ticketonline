package ticket_online.ticket_online.service.impl;
import jakarta.persistence.EntityManager;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.AjAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticket_online.ticket_online.dto.WebResponse;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.repository.EventRepository;
import ticket_online.ticket_online.service.CategoryTicketService;
import ticket_online.ticket_online.service.EventService;

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



    @Override
    public WebResponse<List<Map<String, Object>>> getEventWithMinPrice(Integer total){
        try {
            if(total > 960){
                throw new RuntimeException("Maximum fetch event");
            }
            String sql = "SELECT e.id,e.event_title, e.image, e.description, min(ct.price) as start_from, e.schedule\n" +
                    "\tFROM events e\n" +
                    "\tLEFT JOIN category_tickets ct on e.id = ct.event_id \n" +
                    "\tGROUP BY e.id, e.event_title, e.image, e.description, e.schedule \n" +
                    "LIMIT\t" + total;
            return new WebResponse<>(true, "Event retrieved successfully", jdbcTemplate.queryForList(sql));
        }catch (RuntimeException e){
            return new WebResponse<>(false, e.getMessage(), null);

        }
    }


    @Override
    public WebResponse<Event> getEventById(Long id){
        try {
            Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
//              Event event = eventRepository.findByIdWithJoin(id);

//            Event event = eventRepository.findById(id);
            if(event == null){
                throw new RuntimeException("Event Not Found");
            }
            return new WebResponse<>(true, "Event retrieved", event);
        }catch (RuntimeException e){
            return new WebResponse<>(false,e.getMessage(), null);
        }
    }

    @Override
    public WebResponse<Event> createEventAdmins(Event event){
        try {
            eventRepository.save(event);
            return new WebResponse<>(true, "Event has Creaeted", event);
        }catch (RuntimeException e){
            return new WebResponse<>(false, e.getMessage(), null);
        }
    }

    @Override
    public WebResponse<Boolean> removeEventAdmin(Long id){
        try {
            Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
            //  cek lagi jika event sudah ada transaksi maka tidak boleh di hapus
            event.setIs_active(false);
            eventRepository.save(event);
            return new WebResponse<>(true, "Event has been removed", null);
        }catch (RuntimeException e){
            return new WebResponse<>(false, e.getMessage(), null);
        }
    }


    @Transactional
    @Override
    public Boolean destroyEventAdminWithTickets(Long eventId){
        try {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
             // cek lagi jika event sudah ada transaksi maka tidak boleh di hapus
            eventRepository.deleteById(eventId);
            categoryTicketService.destroyCategoryTicketByEventId(eventId);
            return  true;
        }catch (RuntimeException e){
            System.out.println("Error: " + e.getMessage());
            return  false;
        }
    }


}

