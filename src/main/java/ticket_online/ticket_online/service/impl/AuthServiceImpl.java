package ticket_online.ticket_online.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ticket_online.ticket_online.constant.ERole;
import ticket_online.ticket_online.dto.auth.ChangePasswordReqDto;
import ticket_online.ticket_online.dto.auth.LoginReqDto;
import ticket_online.ticket_online.dto.auth.LoginResDto;
import ticket_online.ticket_online.dto.auth.RegisterReqDto;
import ticket_online.ticket_online.model.AppUser;
import ticket_online.ticket_online.model.User;
import ticket_online.ticket_online.repository.UserRepository;
import ticket_online.ticket_online.service.AuthService;
import ticket_online.ticket_online.security.JwtUtil;
import ticket_online.ticket_online.util.ValidationUtil;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
//    @Transactional(rollbackFor = Exception.class)

    private final ValidationUtil validationUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void register(RegisterReqDto registerReqDto){
        try {
            if(userRepository.findFirstByEmail(registerReqDto.getEmail()).isPresent()){
                throw new RuntimeException("email atau password sudah terdaftar");
            }
            validationUtil.validate(registerReqDto);

            User user = new User();
            user.setFullName(registerReqDto.getFullName());
            user.setEmail(registerReqDto.getEmail());
            user.setPassword(passwordEncoder.encode(registerReqDto.getPassword()));
            user.setBirthDate(registerReqDto.getBirthDate());
            user.setRole(ERole.USER);

            System.out.println(user);
            userRepository.saveAndFlush(user);

        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public LoginResDto login(LoginReqDto loginReqDto){

        validationUtil.validate(loginReqDto);
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReqDto.getEmail(), loginReqDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            AppUser user = (AppUser) authentication.getPrincipal();

            String token = jwtUtil.generateToken(user);
            LoginResDto loginResDto = new LoginResDto();
            User user1 = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("user not found"));

            loginResDto.setToken(token);
            loginResDto.setUserId(user.getId());
            loginResDto.setEmail(user.getEmail());
            loginResDto.setRole(user.getRole());
            loginResDto.setFullName(user1.getFullName());
            return loginResDto;

        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public  void changePassword(ChangePasswordReqDto changePasswordReqDto){
        try {
            Optional<User> user = userRepository.findById(changePasswordReqDto.getUserId());
            if(user.isEmpty()){
                throw new RuntimeException("user tidak ditemukan");
            }

            if(!passwordEncoder.matches(changePasswordReqDto.getOldPassword(), user.get().getPassword())){
                throw new RuntimeException("Password lama salah");
            }
            User user1 = user.get();
            String encodedPassword = passwordEncoder.encode(changePasswordReqDto.getNewPassword());
            user1.setPassword(encodedPassword);
            userRepository.saveAndFlush(user1);
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage(), e);
        }


    }

}
