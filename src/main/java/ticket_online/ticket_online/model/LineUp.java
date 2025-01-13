package ticket_online.ticket_online.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
