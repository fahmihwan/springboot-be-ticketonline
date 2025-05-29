package ticket_online.ticket_online.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ticket_online.ticket_online.model.AppUser;
import ticket_online.ticket_online.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    public User createUser(User user);

    public boolean removeUser(Long userId);

    public User findByUserId(Long userId);

    public User updateUser(Long id, User user);

//    public List<User> getListChecker();
}
