package ticket_online.ticket_online.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ticket_online.ticket_online.dto.categoryTicket.CategoryTicketReqDto;
import ticket_online.ticket_online.dto.categoryTicket.CategoryTicketResDto;
import ticket_online.ticket_online.dto.event.EventLineUpResDto;
import ticket_online.ticket_online.dto.event.EventTicketResDto;
import ticket_online.ticket_online.dto.lineUp.LineUpResDto;
import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.LineUp;
import ticket_online.ticket_online.repository.CategoryTicketRepository;

import ticket_online.ticket_online.repository.EventRepository;
import ticket_online.ticket_online.service.CategoryTicketService;
import ticket_online.ticket_online.service.EventService;
import ticket_online.ticket_online.util.GenerateUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryTicketServiceImpl implements CategoryTicketService {

    @Autowired
    private CategoryTicketRepository categoryTicketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<CategoryTicketResDto> getListCategoryTicketFromSlug(String slug){
            try {
            Optional<Event>  event = eventRepository.findFirstBySlugAndIsActiveTrue(slug);
            if(event.isEmpty()){
                throw new RuntimeException("event is not exists");
            }


            List<CategoryTicket> categoryTickets = categoryTicketRepository.findByEventIdAndIsActiveTrue(event.get().getId());
            return categoryTickets.stream().map(ticket -> CategoryTicketResDto.builder()
                    .id(ticket.getId())
                    .eventId(ticket.getEventId())
                    .categoryName(ticket.getCategoryName())
                    .price(ticket.getPrice())
                    .quotaTicket(ticket.getQuotaTicket())
                    .description(ticket.getDescription())
                    .build()
            ).collect(Collectors.toList());
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage(),e);
        }

    }

    @Override
    public Page<EventTicketResDto> getEventTicketPagination(int page, int size){

        try {

            Pageable pageable = PageRequest.of(page,size);
            Page<Event> response = eventRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable);

            Page<EventTicketResDto> eventTickets = response.map(event -> EventTicketResDto.builder()
                            .id(event.getId())
                            .eventTitle(event.getEvent_title())
                            .venue(event.getVenue())
                            .image(GenerateUtil.generateImgUrl(event.getImage()))
                            .schedule(event.getSchedule())
                            .description(event.getDescription())
                            .createdAt(event.getCreatedAt())
                            .slug(event.getSlug())
                            .categoryTickets(event.getCategory_tickets().stream().filter( ticket-> ticket.getIsActive() == true).collect(Collectors.toList()))
                            .build()
            );
            return eventTickets;
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

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
