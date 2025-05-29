package ticket_online.ticket_online.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ticket_online.ticket_online.dto.checker.ListCheckerResDto;
import ticket_online.ticket_online.dto.event.EventDetailResDto;
import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.repository.EventRepository;
import ticket_online.ticket_online.service.DashboardService;
import ticket_online.ticket_online.util.GenerateUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EventRepository eventRepository;


    @Override
    public Map<String, Object> getDataStatUi(){
        try {
            String sql = "SELECT\n" +
                    "        (select count(t.id) from transactions t ) as total_transaksi,\n" +
                    "        (select count(u.id) from users u ) as total_users,\n" +
                    "        (select count(e.id) from events e) as total_events,\n" +
                    "        (select count(ct.id) from category_tickets ct) as total_ticket";
            List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
            Map<String, Object> result = new HashMap<>();
            result.put("total_transaksi", data.get(0).get("total_transaksi"));
            result.put("total_users", data.get(0).get("total_users"));
            result.put("total_events", data.get(0).get("total_events"));
            result.put("total_ticket", data.get(0).get("total_ticket"));
            return result;
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());

        }
    }

    @Override
    public List<EventResDto> getFiveNewEvent(){
        try {
            List<Event> events = eventRepository.findTop5ByIsActiveTrueOrderByCreatedAtDesc();

            return events.stream()
                    .map(event -> EventResDto.builder()
                            .id(event.getId())
                            .eventTitle(event.getEvent_title())
                            .slug(event.getSlug())
                            .venue(event.getVenue())
                            .image(GenerateUtil.generateImgUrl(event.getImage()))
                            .schedule(event.getSchedule())
                            .description(event.getDescription())
                            .createdAt(event.getCreatedAt())
                            .build()
            ).collect(Collectors.toList());

        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());

        }
    }

    @Override
    public List<Map<String, Object>> getNumberOfTransactionPerMonth(){
        try {
            String sql = "WITH months AS (\n" +
                    "    SELECT generate_series(\n" +
                    "        date_trunc('year', CURRENT_DATE),\n" +
                    "        date_trunc('year', CURRENT_DATE) + interval '11 months',\n" +
                    "        interval '1 month'\n" +
                    "    ) AS month_start\n" +
                    ")\n" +
                    " SELECT \n" +
                    "    TO_CHAR(m.month_start, 'Month') AS month_name,\n" +
                    "    TO_CHAR(m.month_start, 'MM') AS month_number,\n" +
                    "    COUNT(t.id) AS total_transactions\n" +
                    " FROM \n" +
                    "    months m\n" +
                    " LEFT JOIN transactions t \n" +
                    "    ON date_trunc('month', t.created_at) = m.month_start\n" +
                    " GROUP BY \n" +
                    "    m.month_start\n" +
                    " ORDER BY \n" +
                    "    m.month_start;";
            return jdbcTemplate.queryForList(sql);
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());

        }
    }


}
