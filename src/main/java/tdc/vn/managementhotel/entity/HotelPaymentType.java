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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_type_id")
    private PaymentTypes paymentType;

    @Column(name = "deposit_percent")
    private Double depositPercent; // ví dụ: 0.3 = 30% cọc

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_room_id")
    private TypeOfRoom typeOfRoom;

}
