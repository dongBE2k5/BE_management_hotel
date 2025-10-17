package tdc.vn.managementhotel.dto.RoomDTO;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.dto.HotelSummaryDTO;
import tdc.vn.managementhotel.enums.StatusRoom;
import tdc.vn.managementhotel.enums.TypeRoom;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDTO {
    private Long id;
    private String roomNumber;
    private String description;
    private StatusRoom status;
    private TypeRoom typeRoom;
    private BigDecimal price;
    private HotelSummaryDTO hotel; // ✅ thay vì String hotelName
}
