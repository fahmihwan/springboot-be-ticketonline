package ticket_online.ticket_online.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ticket_online.ticket_online.dto.transaction.TransactionDetailHistoriesDto;
import ticket_online.ticket_online.dto.transaction.TransactionHistoriesDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.User;
import ticket_online.ticket_online.repository.EventRepository;
import ticket_online.ticket_online.repository.TransactionRepository;
import ticket_online.ticket_online.repository.UserRepository;
import ticket_online.ticket_online.service.HistoriesTransactionService;
import ticket_online.ticket_online.service.TransactionService;
import ticket_online.ticket_online.util.CheckUtil;
import ticket_online.ticket_online.util.ConvertUtil;
import ticket_online.ticket_online.util.GenerateUtil;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class HistoriesTransactionServiceImpl implements HistoriesTransactionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    //    HISTORIES
    @Override
    public List<TransactionHistoriesDto> transactionHistories(Long userId){


        String sql = "select t.transaction_code, e.image as image, t.transaction_status, e.event_title, t.created_at as tgl_transaksi, t.total_price, t.expiry_period  from transactions t\n" +
                "        inner join detail_transactions dt on t.id = dt.transaction_id\n" +
                "        inner join category_tickets ct on ct.id  = dt.category_ticket_id\n" +
                "        inner join events e on e.id = ct.event_id\n" +
                "        where t.user_id  = ?\n" +
                "        group by t.transaction_code, e.event_title, t.created_at, t.total_price, t.transaction_status, e.image, t.expiry_period ORDER BY t.created_at DESC";

        List<TransactionHistoriesDto>  transactionHistoriesDtos =  jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TransactionHistoriesDto.class), userId);
        for (TransactionHistoriesDto dto : transactionHistoriesDtos){


            Object value = dto.getExpiry_period();
            boolean isExpired = false;
            if (value instanceof Number) {
                Integer expiryPeriod = ((Number) value).intValue(); // ✔️ aman dikonversi
                Timestamp createdAtTimestamp = Timestamp.valueOf(dto.getTgl_transaksi());
                isExpired = CheckUtil.checkIsExpiredTransaction(expiryPeriod, createdAtTimestamp);
            }

            if(isExpired && dto.getTransaction_status().equals("PENDING")){
                String updateSql = "UPDATE transactions SET transaction_status = 'EXPIRED' WHERE transaction_code = ?";
                jdbcTemplate.update(updateSql, dto.getTransaction_code());
            }

            dto.setImage(GenerateUtil.generateImgUrl((String) dto.getImage()));
        }
        return transactionHistoriesDtos;
    }

    //    HISTORIES
    @Override
    public TransactionDetailHistoriesDto transactionDetailHistories(String transactionCode) {

        transactionService.updateStatusTransactionByCode(transactionCode);
        String sql = "select t.id, t.transaction_code, t.pg_payment_url as payment_url, t.pg_va_number as virtual_account, e.image as img, t.event_id, t.user_id, t.transaction_status, t.created_at, " +
                " t.total_qty as total_ticket, t.payment_method, t.pg_payment_amount as total_price\n" +
                " from transactions t\n" +
                " inner join events e on e.id  = t.event_id\n" +
                " where t.transaction_code  = ? limit 1";
        List<TransactionDetailHistoriesDto> transaction =  jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TransactionDetailHistoriesDto.class), transactionCode);

        Optional<User> user = null;
        Optional<Event> event = null;
//
        TransactionDetailHistoriesDto transactionDetailHistoriesDto = new TransactionDetailHistoriesDto();

        if (!transaction.isEmpty()) {
            user = userRepository.findById(transaction.get(0).getUser_id());
            event = eventRepository.findFirstByIdAndIsActiveTrue(transaction.get(0).getEvent_id());

            transactionDetailHistoriesDto.setTransaction_code(transaction.get(0).getTransaction_code());
            transactionDetailHistoriesDto.setTransaction_status(String.valueOf(transaction.get(0).getTransaction_status()));
            LocalDateTime created_at = ConvertUtil.convertToLocalDateTime(transaction.get(0).getCreatedAt());
            transactionDetailHistoriesDto.setTransction_date(created_at);
            transactionDetailHistoriesDto.setPayment_url(transaction.get(0).getPayment_url());
            transactionDetailHistoriesDto.setVirtual_account(transaction.get(0).getVirtual_account());
            transactionDetailHistoriesDto.setTotal_ticket(transaction.get(0).getTotal_ticket());
            transactionDetailHistoriesDto.setTotal_price(transaction.get(0).getTotal_price());
            transactionDetailHistoriesDto.setPayment_method(transaction.get(0).getPayment_method());
            transactionDetailHistoriesDto.setImg(GenerateUtil.generateImgUrl(transaction.get(0).getImg()));

            if (user.isPresent()){
                User mapUser = new User();
                mapUser.setFullName(user.get().getFullName());
                mapUser.setEmail(user.get().getEmail());
                transactionDetailHistoriesDto.setUser(mapUser);
            }

            if(event.isPresent()){
                Event mapEvent = new Event();
                mapEvent.setEvent_title(event.get().getEvent_title());
                LocalDateTime dateSchedule = LocalDateTime.parse(event.get().getSchedule().toString());
                mapEvent.setSchedule(dateSchedule);
                mapEvent.setImage(GenerateUtil.generateImgUrl(event.get().getImage()));
                mapEvent.setVenue(event.get().getVenue());
                mapEvent.setSlug(event.get().getSlug());
                transactionDetailHistoriesDto.setEvent(mapEvent);
            }

        }

        List<Map<String, Object>> getVisitor = transactionRepository.findVisitorFromTransaction(transactionCode);
        List<TransactionDetailHistoriesDto.Participans> participans = new ArrayList<>();
        for (int i = 0; i < getVisitor.size(); i++) {
            TransactionDetailHistoriesDto.Participans participan = new TransactionDetailHistoriesDto.Participans();
            participan.setAddress((String) getVisitor.get(i).get("address"));
            participan.setCategory_name((String) getVisitor.get(i).get("category_name"));

            LocalDate birthDate = LocalDate.parse(getVisitor.get(i).get("birth_date").toString()); // Spring Boot akan mengonversi ISO 8601 string menjadi LocalDateTime
            participan.setBirth_date(birthDate);


            participan.setEmail((String) getVisitor.get(i).get("email"));
            participan.setFull_name((String) getVisitor.get(i).get("full_name"));
            participan.setGender((String) getVisitor.get(i).get("gender"));
            participan.setIncrement_id((Integer) getVisitor.get(i).get("increment_id"));
            participan.setTelp((String) getVisitor.get(i).get("phone_number"));
            participans.add(participan);
        }
        transactionDetailHistoriesDto.setParticipansList(participans);

        return transactionDetailHistoriesDto;

    }

    @Override
    public Map<String, Object>  allOfTransactionFromUsersAdmin(int page, int size) {

        int offset = page * size;

        String sql = "select t.id, t.created_at, t.transaction_code, e.event_title, e.slug, t.total_price, t.total_qty, t.transaction_status, t.payment_method from transactions t \n" +
                "inner join users u on t.user_id  = u.id \n" +
                "inner join events e on e.id  = t.event_id LIMIT ? OFFSET ?";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, size, offset);

        String countSql = "select count(t.id) from transactions t  inner join users u on t.user_id  = u.id inner join events e on e.id  = t.event_id";
        int total = jdbcTemplate.queryForObject(countSql, Integer.class);

        Map<String, Object> result = new HashMap<>();
        result.put("data", data);
        result.put("page", page);
        result.put("size", size);
        result.put("totalElements", total);
        result.put("totalPages", (int) Math.ceil((double) total / size));

        return  result;
    }

}
