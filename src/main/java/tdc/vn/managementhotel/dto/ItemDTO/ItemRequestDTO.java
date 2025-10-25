package tdc.vn.managementhotel.dto.ItemDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDTO {
    private Long id;
    private String name;
    private int defaultQuantity;
}
