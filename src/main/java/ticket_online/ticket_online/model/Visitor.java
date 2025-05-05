package ticket_online.ticket_online.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "visitors")
@ToString
@Getter
@Setter
public class Visitor extends BaseModel{


    @Column(name = "full_name")
    private String fullName;
    private String email;

    @Column(name = "is_primary_visitor")
    private Boolean isPrimaryVisitor;

    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "phone_number")
    private String phoneNumber;
    private String address;

    @Column(name = "is_same_primary_visitor")
    private Long isSamePrimaryVisitor;



//    private String category_name;
//    private String d_birth_date;
//    private String email;
//    private String full_name;
//    private String gender;
//    private Integer increment_id;
//    private Boolean is_same_credential;
//    private Long cart_id;
//    private Boolean isError;
//    private String m_birth_date;
//    private Integer price;
//    private String telp;
//    private String y_birth_date;
}


