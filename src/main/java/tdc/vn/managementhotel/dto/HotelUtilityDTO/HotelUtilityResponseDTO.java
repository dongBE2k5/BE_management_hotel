package tdc.vn.managementhotel.dto.HotelUtilityDTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class HotelUtilityResponseDTO {
    private Long hotelId;
    private List<UtilityItemResponse> utilities = new ArrayList<>();

    @Data
    public static class UtilityItemResponse {
        private Long id;
        private String utilityName;
        private BigDecimal price;
    }
}
