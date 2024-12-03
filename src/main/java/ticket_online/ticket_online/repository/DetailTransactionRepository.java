package ticket_online.ticket_online.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticket_online.ticket_online.model.DetailTransaction;

@Repository
public interface DetailTransactionRepository extends JpaRepository<DetailTransaction, Long> {
}
