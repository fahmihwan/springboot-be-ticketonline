package ticket_online.ticket_online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "detail_transactions")
@ToString
@Getter
@Setter
public class DetailTransaction extends BaseModel {

    private Integer total;

    @Column(name = "user_id")
    private Long userIid;

    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name ="visitor_id")
    private Long visitorId;

    @Column(name = "category_ticket_id")
    private Long categoryTicketId;

    @Column(name = "ticket_code")
    private String ticketCode;


    @ManyToOne
    @JoinColumn(name = "checker_id") // Penting: foreign key ke tabel checker
    private Checker checker;


    @Column(name = "scanned_at")
    private LocalDateTime scannedAt;



}



