package ticket_online.ticket_online.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.LineUp;

import java.util.List;

@Repository
public interface LineUpRepository extends JpaRepository<LineUp, Long> {

    List<LineUp> findByEventIdAndIsActiveTrue(Long id);
}
