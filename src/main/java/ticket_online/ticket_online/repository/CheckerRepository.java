package ticket_online.ticket_online.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.model.Checker;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.User;


import java.util.List;
import java.util.Optional;

@Repository
public interface CheckerRepository extends JpaRepository<Checker,Long> {

    Optional<Checker> findByUserIdAndEventId(User userId, Event eventId);

    List<Checker> findByIsActiveTrueAndEventId_Slug(String slug);

    List<Checker> findByUserId(User userId);

    Optional<Checker> findFirstByUserId(User userId);
}
