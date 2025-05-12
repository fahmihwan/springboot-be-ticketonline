package ticket_online.ticket_online.dto.auth;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterReqDto {
    private String fullName;
    private String email;
    private String password;
//    private String role;
    private LocalDateTime birthDate;
    private String phoneNumber;
}
