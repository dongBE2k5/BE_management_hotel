package tdc.vn.managementhotel.dto.BookingUtilityDTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class BookingUtilityDTO {
    private Long bookingId;
    private List<UtilityItemBooking> utilityItemBooking = new ArrayList<>();

    @Data
    public static class UtilityItemBooking {
        private Long utilityId;
        private Integer quantity;
    }
}
