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
    private Long eventId;

    @Column(name ="category_name")
    private String categoryName;
    private Integer price;

    @Column(name = "quotaTicket")
    private Integer quotaTicket;
    private String description;

}
