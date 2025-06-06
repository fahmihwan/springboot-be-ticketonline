package ticket_online.ticket_online.service.impl;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ticket_online.ticket_online.constant.ERole;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.auth.RegisterReqDto;
import ticket_online.ticket_online.dto.checker.CheckerListEventDto;
import ticket_online.ticket_online.dto.checker.ListCheckerResDto;
import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.dto.transaction.CheckoutReqDto;
import ticket_online.ticket_online.model.*;
import ticket_online.ticket_online.repository.CheckerRepository;
import ticket_online.ticket_online.repository.DetailTransactionRepository;
import ticket_online.ticket_online.repository.EventRepository;
import ticket_online.ticket_online.repository.UserRepository;
import ticket_online.ticket_online.service.AuthService;
import ticket_online.ticket_online.service.CheckerService;
import ticket_online.ticket_online.service.UserService;
import ticket_online.ticket_online.util.CheckUtil;
import ticket_online.ticket_online.util.GenerateUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private DetailTransactionRepository detailTransactionRepository;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> logChecker(Long userId){

        User user = new User();
        user.setId(userId);
        Checker checker = checkerRepository.findFirstByUserId(user).orElseThrow(() -> new RuntimeException("Checker not found"));;


        String sql =  "select dt.id, dt.scanned_at, dt.ticket_code,\n" +
                "v.id, v.full_name, v.address, v.is_primary_visitor, v.birth_date, v.email\n" +
                "from detail_transactions dt\n" +
                "inner join visitors v on v.id = dt.visitor_id\n" +
                "where dt.checker_id = ?";

        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, checker.getId());

        return data;
    }


//    @Override
//    private List<Map, Object> list

//    public ApiResponse<List<Map<String, Object>>> getAllEventUseJDBC() {
//        String sql = "SELECT e.id,e.event_title, e.image, e.description, max(ct.price) as start_from, e.schedule\n" +
//                "\tFROM events e\n" +
//                "\tLEFT JOIN category_tickets ct on e.id = ct.event_id \n" +
//                "\tGROUP BY e.id, e.event_title, e.image, e.description, e.schedule";
//        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
//        return new ApiResponse<>(true, "Event retrieved", data);
//    }
//

    @Override
    public Boolean scanTicket(String ticket_code, Long userId){
        try {

            User user = new User();
            user.setId(userId);
            Checker checker = checkerRepository.findFirstByUserId(user).orElseThrow(() -> new RuntimeException("Checker not found"));;
//            System.out.println(checker);

            String sql = "select dt.* from detail_transactions dt " +
                    "inner join transactions t on dt.transaction_id = t.id " +
                    "where ticket_code = ? and t.transaction_status = 'SUCCESS'";

            List<Map<String, Object>> getTicket = jdbcTemplate.queryForList(sql, ticket_code);
            if(getTicket.size() == 0){
                throw new RuntimeException("Ticket is Not Exists");
            }

            System.out.println(getTicket.get(0));

            Object checkerObj = getTicket.get(0).get("checker_id");
            if(checkerObj != null){
                return  false;
            }

            DetailTransaction detailTransaction = new DetailTransaction();
            detailTransaction.setId((Long) getTicket.get(0).get("id"));
            detailTransaction.setTotal((Integer) getTicket.get(0).get("total"));
            detailTransaction.setUserIid((Long) getTicket.get(0).get("user_id"));
            detailTransaction.setTransactionId((Long) getTicket.get(0).get("transaction_id"));
            detailTransaction.setVisitorId((Long) getTicket.get(0).get("visitor_id"));
            detailTransaction.setCategoryTicketId((Long) getTicket.get(0).get("category_ticket_id"));
            detailTransaction.setTicketCode((String) getTicket.get(0).get("ticket_code"));
            detailTransaction.setChecker(checker);
            detailTransaction.setScannedAt(LocalDateTime.now());
            Timestamp createdAtTimestamp = (Timestamp) getTicket.get(0).get("created_at");
            detailTransaction.setCreatedAt(createdAtTimestamp.toLocalDateTime());


            detailTransaction.setIsActive((Boolean) getTicket.get(0).get("is_active"));
            detailTransactionRepository.save(detailTransaction);

            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<EventResDto> getEventByCheckerUser(Long userId){
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

          List<Checker> checkers =  checkerRepository.findByUserId(user);

            List<EventResDto> eventResDtos = checkers.stream().map(e -> EventResDto.builder()
                    .id(e.getEventId().getId())
                    .eventTitle(e.getEventId().getEvent_title())
                    .image(GenerateUtil.generateImgUrl(e.getEventId().getImage()))
                    .slug(e.getEventId().getSlug())
                    .venue(e.getEventId().getVenue())
                    .schedule(e.getEventId().getSchedule())
                    .description(e.getEventId().getDescription())
                    .build()
            ).collect(Collectors.toList());

            return eventResDtos;
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Page<CheckerListEventDto> getEventCheckerPagination(int page, int size){
        try {
            Pageable pageable = PageRequest.of(page,size);

            Page<Event> response = eventRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable);

            System.out.println(response);
            Page<CheckerListEventDto> checkerListEventDtos = response.map(event -> CheckerListEventDto.builder()
                    .eventTitle(event.getEvent_title())
                    .venue(event.getVenue())
                    .image(GenerateUtil.generateImgUrl(event.getImage()))
                    .schedule(event.getSchedule())
                    .description(event.getDescription())
                    .createdAt(event.getCreatedAt())
                    .slug(event.getSlug())
                    .totalChecker(
                            event.getCheckers().stream().filter(checker -> checker.getIsActive() == true).toList().size()
                    )
                    .build()
            );

            return checkerListEventDtos;
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Override
    public List<ListCheckerResDto> getListChecker(String slug){
            try {
               List<Checker> checkers = checkerRepository.findByIsActiveTrueAndEventId_Slug(slug);


                List<ListCheckerResDto> listCheckerResDto = checkers.stream()
                        .map(checker -> ListCheckerResDto.builder()
                                .id(checker.getId())
                                .fullName(checker.getUserId().getFullName())
                                .email(checker.getUserId().getEmail())
                                .birthDate(checker.getUserId().getBirthDate())
                                .gender(checker.getUserId().getGender())
                                .phoneNumber(checker.getUserId().getPhoneNumber())
                                .address(checker.getUserId().getAddress())
                                .build())
                        .collect(Collectors.toList());

                return  listCheckerResDto;
            }catch (RuntimeException e){
                throw new RuntimeException(e.getMessage(), e);
            }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ListCheckerResDto storeChecker(RegisterReqDto registerReqDto, String slug){

        try {
            Event event = eventRepository.findFirstBySlugAndIsActiveTrue(slug).orElseThrow(() -> new RuntimeException("Event not found"));
            registerReqDto.setPassword("qweqwe123");
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

//            Page<CheckerListEventDto> checkerListEventDtos = response.map(event -> CheckerListEventDto.builder()

            return ListCheckerResDto.builder()
                    .id(checker2.getId())
                    .fullName(checker2.getUserId().getFullName())
                    .email(checker2.getUserId().getEmail())
                    .birthDate(checker2.getUserId().getBirthDate())
                    .gender(checker2.getUserId().getGender())
                    .phoneNumber(checker2.getUserId().getPhoneNumber())
                    .address(checker2.getUserId().getAddress())
                    .build();



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
