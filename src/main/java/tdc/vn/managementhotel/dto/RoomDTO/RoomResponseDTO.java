package tdc.vn.managementhotel.dto.RoomDTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.dto.ImageRoomDTO.ImageRoomResponseDTO;
import tdc.vn.managementhotel.entity.ImageRoom;
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
    private String hotelName;
//    private List<ImageRoomResponseDTO> imageRoom;
    private BigDecimal price;
}