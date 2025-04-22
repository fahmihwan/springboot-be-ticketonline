package ticket_online.ticket_online.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "transactions")
@ToString
@Getter
@Setter
public class Transaction extends BaseModel{

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "total_qty")
    private Integer totalQty;

    @Column(name = "merchant_order_id")
    private String pgMerchantOrderId;

    @Column(name = "merchant_code")
    private String pgMerchantCode;

    @Column(name = "pg_payment_reference")
    private String pgPaymentReference;

    @Column(name = "pg_payment_url")
    private String pgPaymentUrl;

    @Column(name = "pg_va_number")
    private String pgVaNumber;

    @Column(name="pg_payment_amount")
    private Integer pgAmount;

    @Column(name="status_code")
    private String pgStatusCode;

    @Column(name="status_message")
    private String pgStatusMessage;

    @Column(name = "user_id")
    private Long userId;
}
