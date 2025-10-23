package tdc.vn.managementhotel.dto.BookingUtilityDTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BookingUtilityResponseDTO {
    private Long bookingId;
    private List<UtilityItemBookingResponse> utilityItemBookingResponse = new ArrayList<>();

    @Data
    public static class UtilityItemBookingResponse {
        private String utilityName;
        private Integer quantity;
    }
}
