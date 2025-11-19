package tdc.vn.managementhotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tdc.vn.managementhotel.enums.PaymentMethod;
import tdc.vn.managementhotel.enums.PaymentStatus;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    private Long total;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id" ,nullable = false)
    @JsonIgnore
    private Booking booking;

    @Nullable
    private String transactionStatus;

    // 🕒 Thời điểm tạo booking (tự động lưu)
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
