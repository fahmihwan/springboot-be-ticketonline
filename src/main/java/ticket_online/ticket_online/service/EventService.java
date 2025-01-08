package ticket_online.ticket_online.service;


import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.event.EventDetailResDto;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.model.Event;

import java.util.List;
import java.util.Map;

public interface EventService {

    public List<EventHomeResDto> getEventWithMinPrice(Integer total);

    public EventDetailResDto getEventBySlug(String slug);

    public Event getEventWithAllCategoryTickets(Long eventId);

    public Page<Event> getEventPagination(int page, int size);

    public Event createEventAdmin(Event event, MultipartFile image);

    public Event updateEventAdmin(Event event, MultipartFile image, String slug);

    public Boolean removeEventAdmin(Long id);

    public Boolean destroyEventAdminWithTickets(Long eventId);



    ////=================================================================================================================================================================
    public ApiResponse<EventDetailResDto> getEventById(Long id);

}