package tdc.vn.managementhotel.dto.HotelDTO;

import lombok.Data;
import tdc.vn.managementhotel.dto.LocationDTO.LocationResponseDTO;
import tdc.vn.managementhotel.entity.Location;

@Data
public class HotelBookingCountDTO {
    private Long id;
    private String name;
    private String image;
    private LocationResponseDTO location;
    private String status;
    private Long totalBookings;
}
