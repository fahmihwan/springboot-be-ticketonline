package ticket_online.ticket_online.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "line_up")
@ToString
@Getter
@Setter
public class LineUp extends BaseModel{

    @Column(name = "talent_name")
    private String talentName;


    @Column(name ="event_id")
    private Long eventId;

}
