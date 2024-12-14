package ticket_online.ticket_online.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.User;
import ticket_online.ticket_online.repository.EventRepository;
import ticket_online.ticket_online.repository.UserRepository;
import ticket_online.ticket_online.service.QueryExampleService;
import ticket_online.ticket_online.util.ConvertUtil;

import java.sql.Timestamp;
import java.util.*;


@Service
@Slf4j
public class QueryExampleServiceImpl implements QueryExampleService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //pakai ORM
    public List<Event> getEventWithCategories(){
        return eventRepository.findAll();
    }

    //pakai DTO
    public List<EventHomeResDto> getAllEventUseRepo(){
       List<Object[]> results = eventRepository.getAllEventsWithMinPrice();

        List<EventHomeResDto> homeResDtos = new ArrayList<>();

        for (Object[] result : results) {
              Long id = (Long) result[0];
              String event_title = (String) result[1];
              String image = (String) result[2];
              String description = (String) result[3];
              Integer startFrom = (Integer) result[4];
              Timestamp schedule = (Timestamp) result[5];
              System.out.println(schedule);

              final EventHomeResDto eventHomeResDto = new EventHomeResDto();
              eventHomeResDto.setId(id);
              eventHomeResDto.setEvent_title(event_title);
              eventHomeResDto.setImage(image);
              eventHomeResDto.setDescription(description);
              eventHomeResDto.setStart_from(startFrom);
              eventHomeResDto.setSchedule(schedule);
              homeResDtos.add(eventHomeResDto);

        }
       return  homeResDtos;
    }

    //query raw pakai JDBC easy to use
    public List<Map<String, Object>> getAllEventUseJDBC(){
        String sql = "SELECT e.id,e.event_title, e.image, e.description, max(ct.price) as start_from, e.schedule\n" +
                "\tFROM events e\n" +
                "\tLEFT JOIN category_tickets ct on e.id = ct.event_id \n" +
                "\tGROUP BY e.id, e.event_title, e.image, e.description, e.schedule";

        return jdbcTemplate.queryForList(sql);
    }

    //pakai entityManeger (harus di looing ulang, atau di map, males, kalau nga propery nga bakal ikut seperti ini: [[2, "Webinar Teknologi 2024"]])
    public List<Map<String, Object>> getAllEventUseEM(){
        String sql = "SELECT e.id,e.event_title, e.image, e.description, max(ct.price) as start_from, e.schedule\n" +
                "FROM events e\n" +
                "LEFT JOIN category_tickets ct on e.id = ct.event_id \n" +
                "GROUP BY e.id, e.event_title, e.image, e.description, e.schedule";
        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }


    public User findByUserId(Long id){
        System.out.println(id);
        try {
            return userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public Event findByIdCustome(Long id){
        try {
            return eventRepository.findByIdCustome(id);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public Event findByIdJPA(Long id){
        try {
            return eventRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public EventResDto findByIdJpaModelObject(Long id){

        try {
            final Object[] castArray = (Object[]) eventRepository.findByIdCustomeObject(id);

            EventResDto eventResDto = new EventResDto();
            eventResDto.setId((Long) castArray[0]);
            eventResDto.setEventTitle((String) castArray[1]);
            eventResDto.setImage((String) castArray[2]);
            eventResDto.setSchedule(ConvertUtil.convertToLocalDateTime(castArray[3]));
            eventResDto.setDescription((String) castArray[4]);
            eventResDto.setCreatedAt(ConvertUtil.convertToLocalDateTime(castArray[5]));
            return eventResDto;

        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

}
