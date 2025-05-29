package ticket_online.ticket_online.service;

import org.springframework.data.domain.Page;
import ticket_online.ticket_online.dto.auth.RegisterReqDto;
import ticket_online.ticket_online.dto.checker.CheckerListEventDto;
import ticket_online.ticket_online.model.Checker;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.User;

import java.util.List;

public interface CheckerService {

    public Page<CheckerListEventDto> getEventCheckerPagination(int page, int size);

    public Checker storeChecker(RegisterReqDto registerReqDto, String slug);

    public List<Checker> getListChecker(String slug);

    public void removeChecker(Long id);
}
