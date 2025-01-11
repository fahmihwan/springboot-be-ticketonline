package ticket_online.ticket_online.dto.categoryTicket;


import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTicketReqDto {
    private String categoryName;
    private String description;
    private Integer quotaTicket;
    private Integer price;
    private String slug;

}
