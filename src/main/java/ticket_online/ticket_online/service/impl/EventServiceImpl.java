package ticket_online.ticket_online.service.impl;
import jakarta.persistence.EntityManager;

import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.event.EventDetailResDto;
import ticket_online.ticket_online.dto.event.EventHomeResDto;
import ticket_online.ticket_online.dto.event.EventReqDto;
import ticket_online.ticket_online.model.CategoryTicket;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.repository.EventRepository;
import ticket_online.ticket_online.service.CategoryTicketService;
import ticket_online.ticket_online.service.EventService;
import ticket_online.ticket_online.util.ConvertUtil;
import ticket_online.ticket_online.util.GenerateUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
    public List<EventHomeResDto> getEventWithMinPrice(Integer total){
        try {
            if(total > 960){
                throw new RuntimeException("Maximum fetch event");
            }
            String sql = "SELECT e.id,e.event_title, e.image, e.description, min(ct.price) as start_from, e.schedule, e.venue, e.slug \n" +
                    "\tFROM events e\n" +
                    "\tLEFT JOIN category_tickets ct on e.id = ct.event_id \n" +
                    "WHERE e.is_active = true" +
                    "\tGROUP BY e.id, e.event_title, e.image, e.description, e.schedule, e.venue, e.slug \n" +
                    "\tORDER BY e.created_at DESC \n" +
                    "LIMIT\t" + total;

            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> results = query.getResultList();

            List<EventHomeResDto> eventHomeResDtoList = new ArrayList<>();

            for (Object[] row : results) {
//                    log.info("Row data: {}", Arrays.toString(row));

                // Mendapatkan URL gambar secara dinamis menggunakan ServletUriComponentsBuilder
               String imageUrl = GenerateUtil.generateImgUrl((String) row[2]);

                    EventHomeResDto eventHomeResDto = new EventHomeResDto();
                    eventHomeResDto.setId((Long) row[0]);
                    eventHomeResDto.setEvent_title((String) row[1]);
                    eventHomeResDto.setImage(imageUrl);
                    eventHomeResDto.setDescription((String) row[3]);
                    eventHomeResDto.setStart_from((Integer) row[4]);
                    eventHomeResDto.setSchedule(ConvertUtil.convertToLocalDateTime(row[5]));
                    eventHomeResDto.setVenue((String) row[6]);
                    eventHomeResDto.setSlug((String) row[7]);
                    eventHomeResDtoList.add(eventHomeResDto);
            }
            return eventHomeResDtoList;
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());

        }
    }

    @Override
    public EventDetailResDto getEventBySlug(String slug){
        try {
            EventDetailResDto eventDetailResDto = new EventDetailResDto();

            Object[] eventObj = (Object[]) eventRepository.findEventsWithMinPriceBySlug(slug);
                eventDetailResDto.setEventTitle((String) eventObj[1]);
                eventDetailResDto.setImage(GenerateUtil.generateImgUrl((String) eventObj[2]));
                eventDetailResDto.setVenue((String) eventObj[3]);
                eventDetailResDto.setDescription((String) eventObj[4]);
                eventDetailResDto.setStartFromPrice((Integer) eventObj[5]);
                eventDetailResDto.setSchedule(ConvertUtil.convertToLocalDateTime(eventObj[6]));
                eventDetailResDto.setCreatedAt(ConvertUtil.convertToLocalDateTime(eventObj[7]));
                eventDetailResDto.setSlug((String) eventObj[8]);
                return  eventDetailResDto;

        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Override
    public Event getEventWithAllCategoryTickets(String slug){
        System.out.println(slug);
        try {
            return  eventRepository.findFirstBySlugAndIsActiveTrueWithActiveCategoryTickets(slug).orElseThrow(()-> new RuntimeException("Event not found"));
//            return eventRepository.findFirstBySlugAndIsActiveTrue(slug).orElseThrow(()-> new RuntimeException("Event not found"));
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    public Page<Event> getEventPagination(int page, int size){
        try {
            Pageable pageable = PageRequest.of(page,size);
//            Page<Event> response =  eventRepository.getPaginatedEvents(pageable);
//            Page<Event> response = null;
            Page<Event> response = eventRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable);
            response.getContent().forEach(event -> {
                event.setImage(GenerateUtil.generateImgUrl(event.getImage()));
                event.setCategory_tickets(event.getCategory_tickets().stream()
                        .filter(CategoryTicket::getIsActive).collect(Collectors.toList()));
            });


//            List<Event> events = new ArrayList<>();
//            for (Event event : response){
//                String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
//                        .path("/uploaded-images/" + event.getImage())  // Menambahkan path gambar
//                        .toUriString();
//                event.setImage(imageUrl);
//                events.add(event);
//            }
            return response;
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Override
    public Event createEventAdmin(EventReqDto eventReqDto){
        try {

            LocalDateTime dateSchedule = LocalDateTime.parse(eventReqDto.getSchedule()); // Spring Boot akan mengonversi ISO 8601 string menjadi LocalDateTime
            Event event = new Event();
            event.setEvent_title(eventReqDto.getEvent_title());
            event.setSchedule(dateSchedule);
            event.setVenue(eventReqDto.getVenue());
            event.setSlug(eventReqDto.getSlug());
            event.setDescription(eventReqDto.getDescription());
            event.setAdmin_id(eventReqDto.getAdmin_id());

              File dir = new File(UPLOAD_DIR);
              if(!dir.exists()){
                  dir.mkdirs();
              }

              boolean isExistSlug = eventRepository.existsBySlug(event.getSlug());
              if(isExistSlug){
                  throw new RuntimeException("Slug is Exists");
              }

            LocalDateTime now = LocalDateTime.now();// Ambil waktu saat ini menggunakan LocalDateTime

            // Format LocalDateTime menjadi string (misalnya 'yyyyMMddHHmmss')
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String formattedDateTime = now.format(formatter);

//            if(eventReqDto.getImage() != null){

            // Validasi ekstensi file (contoh: hanya gambar .jpg, .png)
            String extension = ConvertUtil.getFileExtension(eventReqDto.getImage().getOriginalFilename()).toLowerCase();
            if (!extension.equals(".jpg") && !extension.equals(".png") && !extension.equals(".jpeg")) {
                throw new RuntimeException("Invalid file type. Only .jpg, .png, .jpeg are allowed.");
            }
            String uniqueFilename = "image_" + formattedDateTime + extension; // Buat nama file unik berdasarkan waktu saat ini

            // Tulis file gambar ke disk
            Path path = Paths.get(UPLOAD_DIR + uniqueFilename); // Tentukan path untuk menyimpan file
            Files.write(path, eventReqDto.getImage().getBytes());
            event.setImage(uniqueFilename);
            eventRepository.save(event);
            return event;
        }catch (RuntimeException | IOException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Override
    public Event updateEventAdmin(EventReqDto eventReqDto, String slug){
        try {

            LocalDateTime dateSchedule = LocalDateTime.parse(eventReqDto.getSchedule()); // Spring Boot akan mengonversi ISO 8601 string menjadi LocalDateTime
            Event event = new Event();
            event.setEvent_title(eventReqDto.getEvent_title());
            event.setSchedule(dateSchedule);
            event.setVenue(eventReqDto.getVenue());
            event.setSlug(eventReqDto.getSlug());
            event.setDescription(eventReqDto.getDescription());
            event.setAdmin_id(eventReqDto.getAdmin_id());

            File dir = new File(UPLOAD_DIR);
            if(!dir.exists()){
                dir.mkdirs();
            }

           Optional<Event> isExistSlug = eventRepository.findFirstBySlugAndIsActiveTrue(slug);

            if(isExistSlug.isEmpty()){
                throw new RuntimeException("Data is not Exists");
            }
            event.setId(isExistSlug.get().getId());


            if(eventReqDto.getImage() != null){
                LocalDateTime now = LocalDateTime.now();// Ambil waktu saat ini menggunakan LocalDateTime
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String formattedDateTime = now.format(formatter);
                String extension = ConvertUtil.getFileExtension(eventReqDto.getImage().getOriginalFilename()).toLowerCase();
                if (!extension.equals(".jpg") && !extension.equals(".png") && !extension.equals(".jpeg")) {
                    throw new RuntimeException("Invalid file type. Only .jpg, .png, .jpeg are allowed.");
                }

                //    delete gambar
                Path oldImagePath = Paths.get(UPLOAD_DIR + event.getImage());
                Files.deleteIfExists(oldImagePath);

                String uniqueFilename = "image_" + formattedDateTime + extension; // Buat nama file unik berdasarkan waktu saat ini
                Path path = Paths.get(UPLOAD_DIR + uniqueFilename); // Tentukan path untuk menyimpan file
                event.setImage(uniqueFilename);


                Files.write(path, eventReqDto.getImage().getBytes());
                event.setImage(uniqueFilename);
            }

            event.setCreatedAt(isExistSlug.get().getCreatedAt());
            event.setIsActive(true);

            eventRepository.save(event);
            return event;
        }catch (RuntimeException | IOException e ){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Boolean removeEventAdmin(Long id){
        try {
            Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
            //  cek lagi jika event sudah ada transaksi maka tidak boleh di hapus
            event.setIsActive(false);
            eventRepository.save(event);
            return true;
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Transactional
    @Override
    public Boolean destroyEventAdminWithTickets(Long eventId){
        try {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
             // cek lagi jika event sudah ada transaksi maka tidak boleh di hapus
            eventRepository.deleteById(eventId);
//            categoryTicketService.destroyCategoryTicketByEventId(eventId);
            return  true;
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    ////=================================================================================================================================================================


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


}

