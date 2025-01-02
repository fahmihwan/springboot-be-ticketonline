package ticket_online.ticket_online.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.repository.CategoryTicketRepository;

import ticket_online.ticket_online.service.CategoryTicketService;
import ticket_online.ticket_online.service.EventService;

import java.util.List;

@Service
public class CategoryTicketServiceImpl implements CategoryTicketService {

    @Autowired
    private CategoryTicketRepository categoryTicketRepository;




    public CategoryTicket createCategoryTicket(CategoryTicket categoryTicket){
        return categoryTicketRepository.save(categoryTicket);
    }

    public Boolean removeCategoryTicketById(Long id){
        try {
            CategoryTicket event = categoryTicketRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
            event.setIs_active(false);
            categoryTicketRepository.save(event);
            return true;
        }catch (RuntimeException e){
            System.out.println("Error: " + e.getMessage());
            return  false;
        }
    }

    public Boolean destroyCategoryTicketByEventId(Long eventId){
        categoryTicketRepository.destroyCategoryTicketByEventId(eventId);
        return true;
    }

}
