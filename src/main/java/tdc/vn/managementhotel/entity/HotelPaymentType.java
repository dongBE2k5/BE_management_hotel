package tdc.vn.managementhotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.PaymentType;

@Entity
@Table(name = "hotel_payment_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelPaymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", length = 20)
    private PaymentType paymentType;

    @Column(name = "deposit_percent")
    private Double depositPercent; // ví dụ: 0.3 = 30% cọc

}
