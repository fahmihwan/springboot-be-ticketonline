package ticket_online.ticket_online.service;

import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.model.Event;

import java.util.List;
import java.util.Map;

public interface EventService {

    public List<Event> getEventWithCategories();

    public List<EventResDto> getAllEvent();

    public List<Map<String, Object>> getAllEventUseJDBC();

    public List<Map<String, Object>> getAllEventUseEM();

    public Event getEventById(Long id);

    public Event createEventAdmins(Event event);

    public Boolean removeEventAdmin(Long id);

    public Boolean destroyEventAdminWithTickets(Long eventId);
}