package ticket_online.ticket_online.service;


import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.event.EventDetailResDto;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.dto.event.EventLineUpResDto;
import ticket_online.ticket_online.dto.event.EventReqDto;
import ticket_online.ticket_online.model.Event;

import java.util.List;
import java.util.Map;

public interface EventService {

    public List<EventHomeResDto> getEventWithMinPrice(Integer total);

    public EventDetailResDto getEventBySlug(String slug);

    public Event getEventWithAllCategoryTickets(String slug);

    public Page<EventLineUpResDto> getEventPagination(int page, int size);

    public Event createEventAdmin(EventReqDto eventReqDto, MultipartFile image);

    public Event updateEventAdmin(EventReqDto eventReqDto, String slug, MultipartFile image);

    public Boolean removeEventAdmin(Long id);

    public Boolean destroyEventAdminWithTickets(Long eventId);

    public ApiResponse<EventDetailResDto> getEventById(Long id);

}