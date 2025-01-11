package ticket_online.ticket_online.service;

import ticket_online.ticket_online.dto.categoryTicket.CategoryTicketReqDto;
import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.model.Event;

import java.util.List;

public interface CategoryTicketService {

    public CategoryTicket createCategoryTicket(CategoryTicketReqDto categoryTicketReqDto);

    public Boolean removeCategoryTicketById(Long id);



}
