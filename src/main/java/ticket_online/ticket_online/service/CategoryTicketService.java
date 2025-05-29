package ticket_online.ticket_online.service;

import org.springframework.data.domain.Page;
import ticket_online.ticket_online.dto.categoryTicket.CategoryTicketReqDto;
import ticket_online.ticket_online.dto.categoryTicket.CategoryTicketResDto;
import ticket_online.ticket_online.dto.checker.CheckerListEventDto;
import ticket_online.ticket_online.dto.event.EventTicketResDto;
import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.model.Event;

import java.util.List;

public interface CategoryTicketService {

    public List<CategoryTicketResDto> getListCategoryTicketFromSlug(String slug);

    public Page<EventTicketResDto> getEventTicketPagination(int page, int size);

    public CategoryTicket createCategoryTicket(CategoryTicketReqDto categoryTicketReqDto);

    public Boolean removeCategoryTicketById(Long id);



}
