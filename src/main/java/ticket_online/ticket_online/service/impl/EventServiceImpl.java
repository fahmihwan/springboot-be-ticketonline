package ticket_online.ticket_online.service.impl;
import jakarta.persistence.EntityManager;

import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.event.EventDetailResDto;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.repository.EventRepository;
import ticket_online.ticket_online.service.CategoryTicketService;
import ticket_online.ticket_online.service.EventService;
import ticket_online.ticket_online.util.ConvertUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@Slf4j
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    CategoryTicketService categoryTicketService;

    @Autowired
    private EntityManager entityManager;

    // Folder untuk menyimpan gambar
    private static String UPLOAD_DIR = "uploaded-images/";



    static {
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs(); // Buat folder jika belum ada
        }
    }

    @Override
    public ApiResponse<List<EventHomeResDto>> getEventWithMinPrice(Integer total){
        try {
            if(total > 960){
                throw new RuntimeException("Maximum fetch event");
            }
            String sql = "SELECT e.id,e.event_title, e.image, e.description, min(ct.price) as start_from, e.schedule, e.venue \n" +
                    "\tFROM events e\n" +
                    "\tLEFT JOIN category_tickets ct on e.id = ct.event_id \n" +
                    "\tGROUP BY e.id, e.event_title, e.image, e.description, e.schedule, e.venue \n" +
                    "LIMIT\t" + total;

            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> results = query.getResultList();

            List<EventHomeResDto> eventHomeResDtoList = new ArrayList<>();

            for (Object[] row : results) {
                    log.info("Row data: {}", Arrays.toString(row));

                    EventHomeResDto eventHomeResDto = new EventHomeResDto();
                    eventHomeResDto.setId((Long) row[0]);
                    eventHomeResDto.setEvent_title((String) row[1]);
                    eventHomeResDto.setImage((String) row[2]);
                    eventHomeResDto.setDescription((String) row[3]);
                    eventHomeResDto.setStart_from((Integer) row[4]);
                    eventHomeResDto.setSchedule(ConvertUtil.convertToLocalDateTime(row[5]));
                    eventHomeResDto.setVenue((String) row[6]);
                    eventHomeResDtoList.add(eventHomeResDto);
            }

            return new ApiResponse<>(true, "Event retrieved successfully", eventHomeResDtoList);
        }catch (RuntimeException e){
            return new ApiResponse<>(false, e.getMessage(), null);

        }
    }


    @Override
    public ApiResponse<EventDetailResDto> getEventById(Long id){
        try {

            Object[] eventObj = (Object[]) eventRepository.findEventsWithMinPriceWhereEventId(id);
            if(eventObj == null){
                throw new RuntimeException("Event Detail Not Found");
            }

            EventDetailResDto eventDetailResDto = new EventDetailResDto();
            eventDetailResDto.setId((Long) eventObj[0]);
            eventDetailResDto.setEventTitle((String) eventObj[1]);
            eventDetailResDto.setImage((String) eventObj[2]);
            eventDetailResDto.setVenue((String) eventObj[3]);
            eventDetailResDto.setDescription((String) eventObj[4]);
            eventDetailResDto.setStartFromPrice((Integer) eventObj[5]);
            eventDetailResDto.setSchedule(ConvertUtil.convertToLocalDateTime(eventObj[6]));
            eventDetailResDto.setCreatedAt(ConvertUtil.convertToLocalDateTime(eventObj[7]));

            return new ApiResponse<>(true, "Event Detail retrieved", eventDetailResDto);
        }catch (RuntimeException e){
            return new ApiResponse<>(false,e.getMessage(), null);
        }
    }


    @Override
    public ApiResponse<Event> getEventWithAllCategoryTickets(Long eventId){
        System.out.println(eventId);
        try {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
            return new ApiResponse<>(true, "Event detail with categories retrieved", event);
        }catch (RuntimeException e){
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<Event> createEventAdmins(Event event, MultipartFile image){
        try {

            String fileName = image.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(image.getInputStream(), path);

            event.setImage(fileName);

            eventRepository.save(event);
            return new ApiResponse<>(true, "Event has Created", event);
        }catch (RuntimeException | IOException e){
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<Boolean> removeEventAdmin(Long id){
        try {
            Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
            //  cek lagi jika event sudah ada transaksi maka tidak boleh di hapus
            event.setIs_active(false);
            eventRepository.save(event);
            return new ApiResponse<>(true, "Event has been removed", null);
        }catch (RuntimeException e){
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }


    @Transactional
    @Override
    public Boolean destroyEventAdminWithTickets(Long eventId){
        try {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
             // cek lagi jika event sudah ada transaksi maka tidak boleh di hapus
            eventRepository.deleteById(eventId);
            categoryTicketService.destroyCategoryTicketByEventId(eventId);
            return  true;
        }catch (RuntimeException e){
            System.out.println("Error: " + e.getMessage());
            return  false;
        }
    }





}

