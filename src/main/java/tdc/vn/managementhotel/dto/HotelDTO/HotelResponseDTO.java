package tdc.vn.managementhotel.dto.HotelDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String locationName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

//    private Integer soPhong;
//    private String userName;
}
