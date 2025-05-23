package ticket_online.ticket_online.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticket_online.ticket_online.dto.categoryTicket.CategoryTicketReqDto;
import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.repository.CategoryTicketRepository;

import ticket_online.ticket_online.repository.EventRepository;
import ticket_online.ticket_online.service.CategoryTicketService;
import ticket_online.ticket_online.service.EventService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryTicketServiceImpl implements CategoryTicketService {

    @Autowired
    private CategoryTicketRepository categoryTicketRepository;

    @Autowired
    private EventRepository eventRepository;


    @Override
    public CategoryTicket createCategoryTicket(CategoryTicketReqDto categoryTicketReqDto){

        try {
            CategoryTicket categoryTicket = new CategoryTicket();
            if(categoryTicketReqDto.getSlug() != null){

              Optional<Event> event = eventRepository.findFirstBySlugAndIsActiveTrue(categoryTicketReqDto.getSlug());
              if(event.isPresent()){
                    categoryTicket.setEventId(event.get().getId());
                    categoryTicket.setCategoryName(categoryTicketReqDto.getCategoryName());
                    categoryTicket.setPrice(categoryTicketReqDto.getPrice());
                    categoryTicket.setIsActive(true);
                    categoryTicket.setDescription(categoryTicketReqDto.getDescription());
                    categoryTicket.setQuotaTicket(categoryTicketReqDto.getQuotaTicket());
                    categoryTicketRepository.save(categoryTicket);
              }else{
                  throw new RuntimeException("event is not Exists");
              }
            }
            return  null;
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }


    }

    @Override
    public Boolean removeCategoryTicketById(Long id){
        try {
            CategoryTicket categoryTicket = categoryTicketRepository.findById(id).orElseThrow(() -> new RuntimeException("Category Ticket not found"));
            categoryTicket.setIsActive(false);
            categoryTicketRepository.save(categoryTicket);
            return true;
        }catch (RuntimeException e){
//            System.out.println("Error: " + e.getMessage());
            return  false;
        }
    }

}
