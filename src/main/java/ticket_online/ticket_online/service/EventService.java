package ticket_online.ticket_online.service;

import ticket_online.ticket_online.model.Event;

import java.util.List;

public interface EventService {

    public List<Event> getAllEvent();

    public Event getEventById(Long id);

    public Event createEventAdmins(Event event);

    public Boolean removeEventAdmin(Long id);

    public Boolean destroyEventAdminWithTickets(Long eventId);
}