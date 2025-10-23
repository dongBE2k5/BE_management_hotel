package tdc.vn.managementhotel.dto.BookingDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.dto.RoomDTO.RoomResponseDTO;
import tdc.vn.managementhotel.dto.UserDTO.UserResponse;
import tdc.vn.managementhotel.entity.Room;
import tdc.vn.managementhotel.entity.User;
import tdc.vn.managementhotel.enums.BookingStatus;
import tdc.vn.managementhotel.enums.StatusRoom;
import tdc.vn.managementhotel.dto.VoucherDTO.VoucherResponseDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private UserResponse user;
    private RoomResponseDTO room;
    private BookingStatus status;
    private BigDecimal totalPrice;
    private String imageHotel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String voucherIds;


}
