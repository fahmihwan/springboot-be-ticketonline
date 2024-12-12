package ticket_online.ticket_online.dao;



import jakarta.persistence.Tuple;
import ticket_online.ticket_online.dto.event.EventHomeResDto;

import java.util.List;

public interface EventDao {

    public List<Tuple> getEventWithCategory();

}
