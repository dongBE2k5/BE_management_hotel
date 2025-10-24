package tdc.vn.managementhotel.dto.ImageRoomDTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ImageRoomRequestDTO {
    private List<String> image = new ArrayList<>();
    private Long hotelId;
    private Long roomTypeId;
}
