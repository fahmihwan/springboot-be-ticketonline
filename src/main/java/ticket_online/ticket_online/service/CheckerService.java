package ticket_online.ticket_online.service;

import org.springframework.data.domain.Page;
import ticket_online.ticket_online.dto.auth.RegisterReqDto;
import ticket_online.ticket_online.dto.checker.CheckerListEventDto;
import ticket_online.ticket_online.dto.checker.ListCheckerResDto;
import ticket_online.ticket_online.dto.event.EventResDto;
import ticket_online.ticket_online.model.Checker;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.User;

import java.util.List;
import java.util.Map;

public interface CheckerService {

    public List<Map<String, Object>> logChecker(Long userId);

    public Boolean scanTicket(String ticket_code, Long userId);

    public List<EventResDto> getEventByCheckerUser(Long id);

    public Page<CheckerListEventDto> getEventCheckerPagination(int page, int size);

    public ListCheckerResDto storeChecker(RegisterReqDto registerReqDto, String slug);

    public List<ListCheckerResDto> getListChecker(String slug);

    public void removeChecker(Long id);
}
