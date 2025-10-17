package tdc.vn.managementhotel.dto.RoomDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.StatusRoom;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomListRequestDTO {
    private List<String> roomNumberList;
    private String description;
    private StatusRoom status;
    private Long typeRoomId;
    private Long hotelId;
    private BigDecimal price;
}
