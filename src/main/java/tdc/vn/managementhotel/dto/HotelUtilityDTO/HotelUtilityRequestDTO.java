package tdc.vn.managementhotel.dto.HotelUtilityDTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class HotelUtilityRequestDTO {
    private Long hotelId;
    private List<UtilityItem> utilities;

    @Data
    public static class UtilityItem {
        private Long utilityId;
        private BigDecimal price;
    }
}
