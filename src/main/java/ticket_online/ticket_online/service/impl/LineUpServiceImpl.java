package ticket_online.ticket_online.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.LineUp;
import ticket_online.ticket_online.repository.EventRepository;
import ticket_online.ticket_online.repository.LineUpRepository;
import ticket_online.ticket_online.service.LineUpService;

import javax.management.relation.RelationNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LineUpServiceImpl implements LineUpService {

    @Autowired
    LineUpRepository lineUpRepository;

    @Autowired
    EventRepository eventRepository;

    public List<LineUp> getAllLineUpBySlug(String slug){
        try {
            Optional<Event>  event = eventRepository.findFirstBySlugAndIsActiveTrue(slug);
            if(event.isEmpty()){
                throw new RuntimeException("event is not exists");
            }
            return  lineUpRepository.findByEventIdAndIsActiveTrue(event.get().getId());
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
//               lineUp.setCreatedAt(getLineUp.get().getCreatedAt());
               lineUp.setIsActive(false);
               lineUpRepository.save(lineUp);
            }

           return true;
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
