package tdc.vn.managementhotel.dto.BookingDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.BookingStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeBookingStatusRequestDTO {
    private Long bookingId;
    private BookingStatus newStatus;
    private Long changedBy;
}
