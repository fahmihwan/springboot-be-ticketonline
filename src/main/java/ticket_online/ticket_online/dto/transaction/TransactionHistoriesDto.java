package ticket_online.ticket_online.dto.transaction;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
public class TransactionHistoriesDto {
    private String transaction_code;
    private String transaction_status;
    private String event_title;
    private LocalDateTime tgl_transaksi;
    private Integer total_price;
    private String image;

}
