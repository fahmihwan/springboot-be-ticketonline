package ticket_online.ticket_online.service;

import ticket_online.ticket_online.dto.Response;
import ticket_online.ticket_online.dto.WebResponse;
import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.model.Event;

import java.util.List;
import java.util.Map;

public interface EventService {

    public WebResponse<List<Map<String, Object>>> getEventWithMinPrice(Integer total);

    public WebResponse<Event> getEventById(Long id);

    public WebResponse<Event> createEventAdmins(Event event);

    public WebResponse<Boolean> removeEventAdmin(Long id);

    public Boolean destroyEventAdminWithTickets(Long eventId);
}