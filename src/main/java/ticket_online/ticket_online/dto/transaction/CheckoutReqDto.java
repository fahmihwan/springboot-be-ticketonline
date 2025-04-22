package ticket_online.ticket_online.dto.transaction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ticket_online.ticket_online.dto.cart.AddCartTicketReqDto;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@ToString
public class CheckoutReqDto {
    private String slug;
    private String paymentMethod;
    private Long userId;
    private List<Participans> participants;
    private List<DetailCarts> detailCartTicket;


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

    @Setter
    @Getter
    @ToString
    public static class DetailCarts{
        private Long category_ticket_id;
        private String category_name;
        private Integer total;
        private Integer price;
    }


}
