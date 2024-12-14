package ticket_online.ticket_online.service;

import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.model.Event;

import java.util.List;

public interface CategoryTicketService {

    public CategoryTicket createCategoryTicket(CategoryTicket categoryTicket);

    public Boolean removeCategoryTicketById(Long id);

    public Boolean destroyCategoryTicketByEventId(Long id);

}
