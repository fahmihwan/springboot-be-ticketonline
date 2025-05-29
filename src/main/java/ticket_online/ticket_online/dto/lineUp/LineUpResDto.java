package ticket_online.ticket_online.dto.lineUp;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LineUpResDto {

    private Long id;
    private String talentName;
}
