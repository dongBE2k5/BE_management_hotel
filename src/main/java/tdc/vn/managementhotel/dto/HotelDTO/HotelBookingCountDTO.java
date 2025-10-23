package tdc.vn.managementhotel.dto.HotelDTO;

import lombok.Data;

@Data
public class HotelBookingCountDTO {
    private Long id;
    private String name;
    private String image;
    private String locationName;
    private String status;
    private Long totalBookings;
}
