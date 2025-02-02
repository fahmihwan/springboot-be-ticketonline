package ticket_online.ticket_online.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.security.PrivilegedAction;
import java.time.LocalDateTime;


@Entity
@Table(name = "users")
@ToString
@Getter
@Setter
public class User extends BaseModel {

    @Column(name = "full_name")
    private String fullName;
    private String email;
    private String password;
    private String role;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Column(name = "phone_number")
    private String phoneNumber;

}
