package tdc.vn.managementhotel.dto.HotelDTO;

import lombok.Data;

@Data
public class HotelDTO {
    private String name;
    private String address;
    private String phone;
    private String image;
    private String email;
    private String status;
    private Long locationId;
    private Long userId;
}
