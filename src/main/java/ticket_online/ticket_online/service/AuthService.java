package ticket_online.ticket_online.service;

import ticket_online.ticket_online.dto.auth.ChangePasswordReqDto;
import ticket_online.ticket_online.dto.auth.LoginReqDto;
import ticket_online.ticket_online.dto.auth.LoginResDto;
import ticket_online.ticket_online.dto.auth.RegisterReqDto;

public interface AuthService {
    void register(RegisterReqDto registerReqDto);

    public LoginResDto login(LoginReqDto loginReqDto);

    void changePassword(ChangePasswordReqDto changePasswordReqDto);
}
