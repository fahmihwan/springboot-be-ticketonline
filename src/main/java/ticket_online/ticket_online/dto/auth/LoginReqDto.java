package ticket_online.ticket_online.dto.auth;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginReqDto {
    private String email;
    private String password;
}
