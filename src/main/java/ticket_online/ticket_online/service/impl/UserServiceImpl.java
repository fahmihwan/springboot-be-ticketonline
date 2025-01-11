package ticket_online.ticket_online.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticket_online.ticket_online.model.User;
import ticket_online.ticket_online.repository.UserRepository;
import ticket_online.ticket_online.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user){
       return userRepository.save(user);
    }

    public boolean removeUser(Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            user.setIsActive(false);
            userRepository.save(user);
            return true;
        }catch (RuntimeException e){
            System.out.println("Error: " + e.getMessage());
            return  false;
        }
    }

    public User findByUserId(Long id) {
        try {
            return userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


}
