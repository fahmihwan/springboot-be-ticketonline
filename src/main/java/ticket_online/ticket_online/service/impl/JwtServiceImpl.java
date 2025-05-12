package ticket_online.ticket_online.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import ticket_online.ticket_online.dto.auth.LoginReqDto;
import ticket_online.ticket_online.dto.auth.LoginResDto;
import ticket_online.ticket_online.model.User;
import ticket_online.ticket_online.repository.UserRepository;
import ticket_online.ticket_online.security.JwtUtil;



@Service
public class JwtServiceImpl {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    public LoginResDto  login(LoginReqDto request){

        try {
            String token="";
            Optional<User>  user = userRepository.findFirstByEmail(request.getEmail());
            if (user.isPresent()){
//                if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
//                    throw new RuntimeException("Invalid password");
//                }
            }


            return (LoginResDto) Map.of("token", token);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage(),e);
        }





//        if ("admin".equals(request.getEmail()) && "admin".equals(request.getPassword())) {
//            String token = jwtUtil.generateToken(request.getUsername());
//            return Map.of("token", token);
//        } else {
//            throw new RuntimeException("Invalid username or password");
//        }
    }
}


