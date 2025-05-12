package ticket_online.ticket_online.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticket_online.ticket_online.dto.ApiResponse;
import ticket_online.ticket_online.dto.auth.ChangePasswordReqDto;
import ticket_online.ticket_online.dto.auth.LoginReqDto;
import ticket_online.ticket_online.dto.auth.LoginResDto;
import ticket_online.ticket_online.dto.auth.RegisterReqDto;
import ticket_online.ticket_online.dto.cart.CartResDto;
import ticket_online.ticket_online.service.AuthService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Boolean>> register(@ModelAttribute RegisterReqDto registerReqDto){
        System.out.println(registerReqDto);
        System.out.println(registerReqDto.getEmail());
        System.out.println("kwkwkkw");
        try {

            System.out.println(registerReqDto);
            authService.register(registerReqDto);
            return  ResponseEntity.ok(new ApiResponse<>(true, "reistrasi berhasi", true));

        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), false));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResDto>> login(@ModelAttribute LoginReqDto loginReqDto){
        try {
           LoginResDto loginResDto = authService.login(loginReqDto);
            return  ResponseEntity.ok(new ApiResponse<>(true, "login berhasi", loginResDto));

        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @PutMapping("/chang-password")
    public ResponseEntity<ApiResponse<Boolean>> changePassword(@ModelAttribute ChangePasswordReqDto changePasswordReqDto){
        try {
            authService.changePassword(changePasswordReqDto);
            return  ResponseEntity.ok(new ApiResponse<>(true, "registrasi berhasi", true));

        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), false));
        }
    }

}
