package ticket_online.ticket_online.repository;

import org.aspectj.weaver.AjAttribute;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.model.Event;

import java.util.List;
import java.util.Optional;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT e.id,e.event_title, e.image, e.description, max(ct.price) as start_from, e.schedule\n" +
            "            FROM events e\n" +
            "            LEFT JOIN category_tickets ct on e.id = ct.event_id \n" +
            "            GROUP BY e.id, e.event_title, e.image, e.description, e.schedule", nativeQuery = true)
    List<Object[]> getAllEventsWithMinPrice();

    @Query(value = "SELECT * FROM events WHERE id = :id", nativeQuery = true)
    Event findByIdCustome(Long id);

    @Query(value = "SELECT id, event_title, image, schedule, description, created_at  FROM events WHERE id = :id", nativeQuery = true)
    Object findByIdCustomeObject(Long id);

    //opsi bikin native query aja, gmna?
    //kalau pakai query native dan return Entitynya tetap ORM







}
