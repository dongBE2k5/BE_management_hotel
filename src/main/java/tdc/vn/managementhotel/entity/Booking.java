package tdc.vn.managementhotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.List;

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
    @JsonIgnore
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="room_id", nullable = false)
    @JsonIgnore
    private Room room;

    //  Thêm voucher nếu có
    @Column(length = 255)
    private String voucherIds; // lưu chuỗi ID, ví dụ: ["1","2"]

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_type_id")
    private HotelPaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private BigDecimal totalPrice;

    // 🕒 Thời điểm tạo booking (tự động lưu)
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 🕒 Thời điểm cập nhật trạng thái
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "booking",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomAssignment> roomAssignments;

    @OneToMany(mappedBy = "booking",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DamagedItem> damagedItems;

}