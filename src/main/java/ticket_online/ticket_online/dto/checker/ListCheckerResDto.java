package ticket_online.ticket_online.dto.checker;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ticket_online.ticket_online.constant.EGender;
import ticket_online.ticket_online.constant.ERole;

import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListCheckerResDto {
    private Long id;
    private String fullName;
    private String email;
    private EGender gender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;
}
