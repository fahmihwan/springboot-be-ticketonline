package ticket_online.ticket_online.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ticket_online.ticket_online.constant.ERole;
import ticket_online.ticket_online.model.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResDto {
    private String token;
    private Long userId;
    private String email;
    private ERole role;
    private String fullName;


}
