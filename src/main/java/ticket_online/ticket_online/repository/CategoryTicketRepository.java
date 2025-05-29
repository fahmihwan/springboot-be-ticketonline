package ticket_online.ticket_online.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.LineUp;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryTicketRepository extends JpaRepository<CategoryTicket,Long> {

    Optional<CategoryTicket> findFirstByIdAndIsActiveTrue(Long eventId);

    List<CategoryTicket> findByEventIdAndIsActiveTrue(Long id);

//    int updateQuotaTicketById(Long id, Integer quotaTicket);

    //    @Modifying //@Modifying: Menandakan bahwa query ini adalah query yang mengubah data (misalnya, INSERT, UPDATE, atau DELETE).
//    @Modifying
//    @Query("DELETE FROM CategoryTicket WHERE event_id = :eventId")
//    public void destroyCategoryTicketByEventId(Long eventId);

//    Page<Event> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);
//    List<CategoryTicket> findByEventIdAndIsActiveTrueOrderByCreatedAtDesc(Long id);

}


