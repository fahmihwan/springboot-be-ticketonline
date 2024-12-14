package ticket_online.ticket_online.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "category_tickets")
@ToString
@Getter
@Setter
public class CategoryTicket extends BaseModel {

    @Column(name ="event_id")
    private Long event_id;
    private String category_name;
    private Integer price;

    @Column(name = "quotaTicket")
    private Integer quotaTicket;
    private String description;

}
