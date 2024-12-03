package ticket_online.ticket_online.service;

import ticket_online.ticket_online.model.User;

public interface UserService {

    public User createUser(User user);

    public boolean removeUser(Long userId);

    public User findByUserId(Long userId);

}
