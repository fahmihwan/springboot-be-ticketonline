package ticket_online.ticket_online.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ticket_online.ticket_online.model.AppUser;
import ticket_online.ticket_online.model.User;

public interface UserService extends UserDetailsService {

    public User createUser(User user);

    public boolean removeUser(Long userId);

    public User findByUserId(Long userId);


}
