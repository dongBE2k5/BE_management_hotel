package tdc.vn.managementhotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import tdc.vn.managementhotel.enums.BookingStatus;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="room_id", nullable = false)
    private Room room;

    // ✅ Thêm voucher nếu có
    @ManyToOne
    @JoinColumn(name = "voucher_id", nullable = true)
    private Voucher voucher;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private BigDecimal totalPrice;

    // 🕒 Thời điểm tạo booking (tự động lưu)
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 🕒 Thời điểm cập nhật trạng thái
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}