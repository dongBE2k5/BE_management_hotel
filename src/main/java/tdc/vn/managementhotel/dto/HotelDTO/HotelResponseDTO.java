package tdc.vn.managementhotel.dto.HotelDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String locationName;
//    private String userName;
}
