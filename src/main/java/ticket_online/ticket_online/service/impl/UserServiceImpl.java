package ticket_online.ticket_online.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ticket_online.ticket_online.constant.ERole;
import ticket_online.ticket_online.model.AppUser;
import ticket_online.ticket_online.model.User;
import ticket_online.ticket_online.repository.UserRepository;
import ticket_online.ticket_online.service.UserService;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService  {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user){
       return userRepository.save(user);
    }

    @Override
    public boolean removeUser(Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            user.setIsActive(false);
            userRepository.save(user);
            return true;
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public User findByUserId(Long id) {
        try {
            return userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findFirstByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Credential"));

        return AppUser.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();

    }



}
