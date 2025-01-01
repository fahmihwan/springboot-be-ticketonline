package ticket_online.ticket_online.dto.transaction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GetPaymentMethodReqDto {
    private Integer amount;
}



