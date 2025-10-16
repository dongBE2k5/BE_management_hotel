package tdc.vn.managementhotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingUtility extends BaseEntity {

    @EmbeddedId
    private BookingUtilityID id = new BookingUtilityID();
    @ManyToOne
    @MapsId("bookingId")
    @JoinColumn(name = "booking_id")
    private Booking booking;
    @ManyToOne
    @MapsId("utilityId")
    @JoinColumn(name = "utility_id")
    private Utility utility;
    private Integer quantity;
}
