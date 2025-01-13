package ticket_online.ticket_online.service;

import ticket_online.ticket_online.model.LineUp;

import java.util.List;

public interface LineUpService {

    public List<LineUp> getAllLineUpBySlug(String slug);

    public LineUp createLineUp(LineUp lineUp, String slug);

    public Boolean removeLineup(Long id);
}
