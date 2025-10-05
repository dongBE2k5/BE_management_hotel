package tdc.vn.managementhotel.dto.RoomDTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import tdc.vn.managementhotel.dto.ImageRoomDTO.ImageRoomRequestDTO;
import tdc.vn.managementhotel.enums.StatusRoom;

@Data
public class RoomRequestDTO {
    private String roomNumber;
    private String description;
    private StatusRoom status;
    private Long typeRoomId;
    private Long hotelId;
    private List<ImageRoomRequestDTO> imageRoom;
    private BigDecimal price;
}
