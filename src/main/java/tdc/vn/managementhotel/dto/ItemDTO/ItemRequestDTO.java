package tdc.vn.managementhotel.dto.ItemDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.User;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long hotelId;
}
