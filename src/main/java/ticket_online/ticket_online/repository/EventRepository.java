package ticket_online.ticket_online.repository;

import org.aspectj.weaver.AjAttribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    boolean existsBySlug(String slug); //query method

    Optional<Event> findFirstBySlugAndIsActiveTrue(String slug);


    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.category_tickets ct WHERE e.slug = :slug AND e.isActive = true AND ct.isActive = true")
    Optional<Event> findFirstBySlugAndIsActiveTrueWithActiveCategoryTickets(@Param("slug") String slug);


    // Query kustom dengan pagination
//    @Query("SELECT e FROM Event e WHERE e.is_active = true ORDER BY e.created_at DESC")
//    Page<Event> getPaginatedEvents(Pageable pageable);

    Page<Event> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);

    @Query(value = "SELECT e.id,e.event_title, e.image, e.description, max(ct.price) as start_from, e.schedule\n" +
            "            FROM events e\n" +
            "            LEFT JOIN category_tickets ct on e.id = ct.event_id \n" +
            "            GROUP BY e.id, e.event_title, e.image, e.description, e.schedule", nativeQuery = true)
    List<Object[]> getAllEventsWithMinPrice();

    @Query(value = "SELECT e.id,e.event_title, e.image, e.venue, e.description, max(ct.price) as start_from_price, e.schedule, e.created_at, e.slug\n" +
            "\t\tFROM events e\n" +
            "\t\tLEFT JOIN category_tickets ct on e.id = ct.event_id\n" +
            "\t\tWHERE e.slug= :slug\n AND e.is_active = true" +
            "\t\tGROUP BY e.id, e.event_title, e.image, e.venue, e.description, e.schedule, e.created_at, e.slug" +
            "\t\tLIMIT 1", nativeQuery = true)
    Object findEventsWithMinPriceBySlug(String slug);


    @Query(value = "SELECT * FROM events WHERE id = :id", nativeQuery = true)
    Event findByIdCustome(Long id);

    @Query(value = "SELECT id, event_title, image, venue, schedule, description, created_at  FROM events WHERE id = :id", nativeQuery = true)
    Object findByIdCustomeObject(Long id);


//=================================================================================================================================================================
    @Query(value = "SELECT e.id,e.event_title, e.image, e.venue, e.description, max(ct.price) as start_from_price, e.schedule, e.created_at\n" +
            "\t\tFROM events e\n" +
            "\t\tLEFT JOIN category_tickets ct on e.id = ct.event_id\n" +
            "\t\tWHERE e.id = :id\n" +
            "\t\tGROUP BY e.id, e.event_title, e.image, e.venue, e.description, e.schedule, e.created_at", nativeQuery = true)
    Object findEventsWithMinPriceWhereEventId(Long id);








}
