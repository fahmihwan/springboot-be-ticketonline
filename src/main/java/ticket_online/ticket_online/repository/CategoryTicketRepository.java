package ticket_online.ticket_online.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ticket_online.ticket_online.model.CategoryTicket;

@Repository
public interface CategoryTicketRepository extends JpaRepository<CategoryTicket,Long> {

    //    @Modifying //@Modifying: Menandakan bahwa query ini adalah query yang mengubah data (misalnya, INSERT, UPDATE, atau DELETE).
    @Modifying
    @Query("DELETE FROM CategoryTicket WHERE event_id = :event_id")
    public void destroyCategoryTicketByEventId(Long event_id);
}


