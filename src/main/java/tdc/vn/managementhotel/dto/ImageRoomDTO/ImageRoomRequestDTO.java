package tdc.vn.managementhotel.dto.ImageRoomDTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ImageRoomRequestDTO {
    private Long hotelId;
    private Long roomTypeId;
    private List<Long> deletedImageIds; // thêm mới

}
