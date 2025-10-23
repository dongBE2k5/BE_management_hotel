package tdc.vn.managementhotel.dto.TypeOfRoomDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.dto.ImageRoomDTO.ImageRoomResponseDTO;
import tdc.vn.managementhotel.entity.ImageRoom;
import tdc.vn.managementhotel.enums.TypeRoom;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfRoomDTO {
    private Long id;
    private TypeRoom room;
    private List<ImageRoomResponseDTO> imageRooms;
}
