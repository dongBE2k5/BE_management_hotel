package tdc.vn.managementhotel.dto.VoucherDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherResponseDTO {
    private Long id;
    private String code;
    private Long hotelId;
    private String description;
    private Long  priceCondition;
    private Integer percent;
    private Integer quantity;
    private Integer used;


}
