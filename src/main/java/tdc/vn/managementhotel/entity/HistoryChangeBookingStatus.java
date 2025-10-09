package tdc.vn.managementhotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.BookingStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HistoryChangeBookingStatus extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="booking_id", nullable = false)
    private Booking booking;
    @Enumerated(EnumType.STRING)
    private BookingStatus oldStatus;
    @Enumerated(EnumType.STRING)
    private BookingStatus newStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="changeBy_id", nullable = false)
    private User changedBy;
}
