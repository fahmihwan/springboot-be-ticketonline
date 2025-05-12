package ticket_online.ticket_online.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ticket_online.ticket_online.constant.ERole;

import java.security.PrivilegedAction;
import java.time.LocalDateTime;


@Entity
@Table(name = "users")
@ToString(exclude = "role")
@Getter
@Setter
public class User extends BaseModel {

    @Column(name = "full_name")
    private String fullName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Column(name = "phone_number")
    private String phoneNumber;





}
