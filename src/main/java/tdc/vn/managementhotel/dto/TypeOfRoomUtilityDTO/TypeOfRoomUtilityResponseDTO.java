package tdc.vn.managementhotel.dto.TypeOfRoomUtilityDTO;

import lombok.Data;
import tdc.vn.managementhotel.enums.TypeRoom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class TypeOfRoomUtilityResponseDTO {
    private List<UtilityItemResponse> utilities = new ArrayList<>();

    @Data
    public static class UtilityItemResponse {
        private Long id;
        private String utilityName;
        private BigDecimal price;
        private Long typeOfRoomId;
        private TypeRoom typeOfRoom;
        private String imageUrl;
    }
}
