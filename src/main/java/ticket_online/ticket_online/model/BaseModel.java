package ticket_online.ticket_online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@ToString
public class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Boolean is_active;

    private LocalDateTime created_at;

    @PrePersist
    public void prePersist(){
        if(created_at == null){
            this.created_at = LocalDateTime.now();
            this.is_active = true;
        }
    }
}
