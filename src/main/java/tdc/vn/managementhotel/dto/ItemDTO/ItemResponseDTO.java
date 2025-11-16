package tdc.vn.managementhotel.dto.ItemDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDTO {
    private Long id;
    private  String name;
//    private int defaultQuantity;
    private BigDecimal price;
    private Long HotelId;

}
