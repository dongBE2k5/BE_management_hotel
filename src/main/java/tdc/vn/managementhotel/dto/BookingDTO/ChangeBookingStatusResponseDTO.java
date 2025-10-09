package tdc.vn.managementhotel.dto.BookingDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.dto.UserDTO.UserResponse;
import tdc.vn.managementhotel.entity.Booking;
import tdc.vn.managementhotel.enums.BookingStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeBookingStatusResponseDTO {
    private Long id;
    private BookingResponseDTO booking;
    private BookingStatus oldStatus;
    private BookingStatus newStatus;
    private LocalDateTime createdAt;
    private UserResponse changedBy;
}
