package ticket_online.ticket_online.dto.transaction;

import jakarta.persistence.Column;
import jakarta.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ticket_online.ticket_online.model.Event;
import ticket_online.ticket_online.model.User;
import ticket_online.ticket_online.model.Visitor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Setter
@Getter
@ToString
public class TransactionDetailHistoriesDto {

    private String transaction_code;
    private String transaction_status;
    private LocalDateTime transction_date;
    private Integer total_ticket;
    private String payment_method;
    private String virtual_account;
    private String payment_url;
    private Integer total_price;
    private String img;

    private Long user_id;
    private Long event_id;
    private User user;
    private Event event;

    private LocalDateTime createdAt;

    private List<Participans> participansList;

    @Setter
    @Getter
    @ToString
    public static class Participans {
        private String address;
        private String category_name;
        private Long category_ticket_id;
        private LocalDate birth_date;
        private String email;
        private String full_name;
        private String gender;
        private Integer increment_id;
        private Boolean is_same_credential;
        private Long cart_id;
        private Boolean isError;
        private Integer price;
        private String telp;
    }


}
