package ticket_online.ticket_online.service;

import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.model.Event;

import java.util.List;
import java.util.Map;

public interface DashboardService {
//    getStats();
    public Map<String, Object> getDataStatUi();

    public List<EventResDto> getFiveNewEvent();

    public List<Map<String, Object>> getNumberOfTransactionPerMonth();
}
