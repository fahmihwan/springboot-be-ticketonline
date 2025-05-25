package ticket_online.ticket_online.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ticket_online.ticket_online.constant.EGender;
import ticket_online.ticket_online.constant.ERole;

import java.security.PrivilegedAction;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "users")
@ToString(exclude = {"role", "gender"})
@Getter
@Setter
public class User extends BaseModel {

    @Column(name = "full_name")
    private String fullName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private EGender gender;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String address;





}
