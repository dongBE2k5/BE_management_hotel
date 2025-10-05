package tdc.vn.managementhotel.dto.ImageRoomDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageRoomResponseDTO {
    private Long id;
    private String image;
    private Long roomTypeId;
}
