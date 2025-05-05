package ticket_online.ticket_online.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ticket_online.ticket_online.model.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT v.*, ct.category_name, e.image FROM transactions t\n" +
            " INNER JOIN detail_transactions dt on t.id = dt.transaction_id\n" +
            " inner join category_tickets ct on dt.category_ticket_id = ct.id \n" +
            " inner join events e on e.id = ct.event_id \n" +
            " INNER JOIN visitors v on v.id = dt.visitor_id  WHERE t.transaction_code = :transactionCode ", nativeQuery = true)
    List<Map<String, Object>> findVisitorFromTransaction(String transactionCode);


    @Query(value = "select t.id, t.transaction_code, e.image, t.event_id, t.transaction_status, t.created_at, t.total_qty, t.payment_method, t.pg_payment_amount\n" +
            "from transactions t\n" +
            "inner join events e on e.id  = t.event_id \n" +
            "where t.transaction_code  = :transactionCode", nativeQuery = true)
    Optional<Object[]> findFirstByTransactionCodeSQL(String transactionCode);





}
