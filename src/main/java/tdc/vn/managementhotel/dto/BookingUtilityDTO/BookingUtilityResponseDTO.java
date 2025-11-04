package tdc.vn.managementhotel.dto.BookingUtilityDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingUtilityResponseDTO {
    private Long bookingId;
    private List<UtilityItemBookingResponse> utilityItemBookingResponse = new ArrayList<>();

    @Data
    @Builder
    public static class UtilityItemBookingResponse {
        private String utilityName;
        private Integer quantity;
    }
}
