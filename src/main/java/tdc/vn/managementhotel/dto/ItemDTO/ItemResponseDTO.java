package tdc.vn.managementhotel.dto.ItemDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDTO {
    private  String name;
    private int defaultQuantity;
}
