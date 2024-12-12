package ticket_online.ticket_online.dao.impl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ticket_online.ticket_online.dao.EventDao;
import ticket_online.ticket_online.dto.event.EventHomeResDto;


import java.util.List;

@Repository
public class EventDaoImpl implements EventDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Tuple> getEventWithCategory(){

        final String sql = "select e.id,e.event_title, e.image, e.description, max(ct.price) as start_from\n" +
                "from events e left join category_tickets ct on e.id = ct.event_id \n" +
                "group  by  e.id,e.event_title, e.image, e.description\n";

        Query query = em.createNativeQuery(sql,"EventHomeResDto");

        List<Tuple> resultList = query.getResultList();
        return resultList;
    }
}
