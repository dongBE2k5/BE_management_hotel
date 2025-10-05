package tdc.vn.managementhotel.dto.ImageRoomDTO;

import lombok.Data;

@Data
public class ImageRoomRequestDTO {
    private String image;
    private Long hotelId;
    private Long roomTypeId;
}
