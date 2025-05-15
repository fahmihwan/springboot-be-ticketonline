package ticket_online.ticket_online.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ticket_online.ticket_online.constant.EGender;
import ticket_online.ticket_online.constant.ERole;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterReqDto {
    private String fullName;
    private String email;
    private String password;
    private ERole role;
    private EGender gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;
}
