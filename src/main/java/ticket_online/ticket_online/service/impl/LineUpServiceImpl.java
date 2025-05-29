package ticket_online.ticket_online.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ticket_online.ticket_online.dto.checker.ListCheckerResDto;
import ticket_online.ticket_online.dto.event.EventLineUpResDto;
import ticket_online.ticket_online.dto.lineUp.LineUpResDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.LineUp;
import ticket_online.ticket_online.repository.EventRepository;
import ticket_online.ticket_online.repository.LineUpRepository;
import ticket_online.ticket_online.service.LineUpService;
import ticket_online.ticket_online.util.GenerateUtil;

import javax.management.relation.RelationNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LineUpServiceImpl implements LineUpService {

    @Autowired
    LineUpRepository lineUpRepository;

    @Autowired
    EventRepository eventRepository;

    public List<LineUpResDto> getAllLineUpBySlug(String slug){
        try {
            Optional<Event>  event = eventRepository.findFirstBySlugAndIsActiveTrue(slug);
            if(event.isEmpty()){
                throw new RuntimeException("event is not exists");
            }

            List<LineUp> lineUp = lineUpRepository.findByEventIdAndIsActiveTrue(event.get().getId());

            return lineUp.stream().map(lineUp1 -> LineUpResDto.builder()
                    .id(lineUp1.getId())
                    .talentName(lineUp1.getTalentName())
                    .build()
            ).collect(Collectors.toList());
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    public LineUp createLineUp(LineUp lineUp, String slug){
        try {
           Optional<Event> event = eventRepository.findFirstBySlugAndIsActiveTrue(slug);
           if(event.isEmpty()){
               throw new RuntimeException("event is not exists");
           }
           lineUp.setEventId(event.get().getId());
           lineUpRepository.save(lineUp);
           return lineUp;
       }catch (RuntimeException e){
           throw new RuntimeException(e.getMessage(),e);
       }
    }

    public Boolean removeLineup(Long id){
        try {
            Optional<LineUp> getLineUp = lineUpRepository.findById(id);
            if(getLineUp.isPresent()){
                LineUp lineUp = new LineUp();
               lineUp.setId(getLineUp.get().getId());
               lineUp.setEventId(getLineUp.get().getEventId());
               lineUp.setTalentName(getLineUp.get().getTalentName());
               lineUp.setIsActive(false);
               lineUpRepository.save(lineUp);
            }

           return true;
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
