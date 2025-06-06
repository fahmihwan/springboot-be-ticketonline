package ticket_online.ticket_online.dto.checker;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ScanTicketReqDto {

    private String ticket_code;
    private Long userId;
}
