package ticket_online.ticket_online.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "events")
@ToString
@Getter
@Setter
public class Event extends BaseModel{

    private String event_title;

    @Column(name = "slug")
    private String slug;
    private String image;
    private LocalDateTime schedule;
    private String description;

    @JsonIgnore
    private Long admin_id;
    private String venue;


//    @JoinColumn(name = "event_id", referencedColumnName = "id")
//    @JsonIgnore // supaya bisa without join
    @OneToMany()
    @JoinColumn(name = "event_id")
    private List<CategoryTicket> category_tickets = new ArrayList<CategoryTicket>();


}
