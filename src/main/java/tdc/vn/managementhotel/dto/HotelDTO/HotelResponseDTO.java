package tdc.vn.managementhotel.dto.HotelDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.dto.LocationDTO.LocationResponseDTO;
import tdc.vn.managementhotel.entity.Hotel;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String image;
    private String email;
    private String status;
    private LocationResponseDTO location;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    // private String userName;

    // ✅ Thêm constructor nhận Hotel entity
    public HotelResponseDTO(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.address = hotel.getAddress();
        this.phone = hotel.getPhone();
        this.image = hotel.getImage();
        this.email = hotel.getEmail();
        this.status = hotel.getStatus();
//        this.locationName = hotel.getLocation() != null ? hotel.getLocation().getName() : null;
        // this.userName = hotel.getUser() != null ? hotel.getUser().getName() : null;
    }
}