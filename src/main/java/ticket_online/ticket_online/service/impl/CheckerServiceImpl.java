package ticket_online.ticket_online.service.impl;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ticket_online.ticket_online.constant.ERole;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.auth.RegisterReqDto;
import ticket_online.ticket_online.dto.checker.CheckerListEventDto;
import ticket_online.ticket_online.dto.transaction.CheckoutReqDto;
import ticket_online.ticket_online.model.*;
import ticket_online.ticket_online.repository.CheckerRepository;
import ticket_online.ticket_online.repository.EventRepository;
import ticket_online.ticket_online.repository.UserRepository;
import ticket_online.ticket_online.service.AuthService;
import ticket_online.ticket_online.service.CheckerService;
import ticket_online.ticket_online.service.UserService;
import ticket_online.ticket_online.util.GenerateUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class CheckerServiceImpl implements CheckerService {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CheckerRepository checkerRepository;


    @Override
    public Page<CheckerListEventDto> getEventCheckerPagination(int page, int size){
        try {
            Pageable pageable = PageRequest.of(page,size);

            Page<Event> response = eventRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable);

            Page<CheckerListEventDto> checkerListEventDtos = response.map(event -> CheckerListEventDto.builder()
                    .eventTitle(event.getEvent_title())
                    .venue(event.getVenue())
                    .image(GenerateUtil.generateImgUrl(event.getImage()))
                    .schedule(event.getSchedule())
                    .description(event.getDescription())
                    .createdAt(event.getCreatedAt())
                    .slug(event.getSlug())
                    .totalChecker(event.getCheckers().size())
                    .build()
            );

            return checkerListEventDtos;
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Override
    public List<Checker> getListChecker(String slug){
            try {
               List<Checker> checkers = checkerRepository.findByIsActiveTrueAndEventId_Slug(slug);
                return  checkers;
            }catch (RuntimeException e){
                throw new RuntimeException(e.getMessage(), e);
            }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Checker storeChecker(RegisterReqDto registerReqDto, String slug){

        try {
            Event event = eventRepository.findFirstBySlugAndIsActiveTrue(slug).orElseThrow(() -> new RuntimeException("Event not found"));
            registerReqDto.setPassword("qwewqe123");
            registerReqDto.setRole(ERole.CHECKER);

            Long userId = authService.register(registerReqDto);
            User user = new User();
            user.setId(userId);

           Optional<Checker> checker =  checkerRepository.findByUserIdAndEventId(user, event);
           if(checker.isPresent()){
               throw new RuntimeException("User exists");
           }


            Checker checker1 = new Checker();
            checker1.setEventId(event);
            checker1.setUserId(user);
            Checker checker2 = checkerRepository.save(checker1);

            return checker2;
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeChecker(Long id){
        try {

           Checker checker = checkerRepository.findById(id).orElseThrow(() -> new RuntimeException("Checker not found"));
           checker.setIsActive(false);
           checkerRepository.save(checker);

            User user = userRepository.findById(checker.getUserId().getId()).orElseThrow(() -> new RuntimeException("User not found not found"));
            user.setIsActive(false);
            userRepository.save(user);

        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }


}
