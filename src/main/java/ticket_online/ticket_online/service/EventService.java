package ticket_online.ticket_online.service;

import org.springframework.web.multipart.MultipartFile;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.event.EventDetailResDto;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.model.Event;

import java.util.List;
import java.util.Map;

public interface EventService {

    public ApiResponse<List<EventHomeResDto>> getEventWithMinPrice(Integer total);

    public ApiResponse<EventDetailResDto> getEventById(Long id);

    public ApiResponse<Event> getEventWithAllCategoryTickets(Long eventId);

    public ApiResponse<Event> createEventAdmins(Event event, MultipartFile image);

    public ApiResponse<Boolean> removeEventAdmin(Long id);

    public Boolean destroyEventAdminWithTickets(Long eventId);


}