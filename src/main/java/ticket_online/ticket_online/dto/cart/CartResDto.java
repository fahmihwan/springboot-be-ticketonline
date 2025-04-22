package ticket_online.ticket_online.dto.cart;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartResDto {
    private Long id;
    private Long category_ticket_id;
    private String category_name;
    private Integer price;
    private Integer total;
}
