package ticket_online.ticket_online.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordReqDto {
//    Long userId, String oldPassword, String newPassword
    private Long userId;
    private String oldPassword;
    private String newPassword;

}
