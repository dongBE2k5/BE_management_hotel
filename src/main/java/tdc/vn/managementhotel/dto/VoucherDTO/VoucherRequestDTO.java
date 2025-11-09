package tdc.vn.managementhotel.dto.VoucherDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRequestDTO {
    private String code;
    private String name;
    private String description;
    private Long priceCondition;
    private Long hotelId;
    private Integer quantity;
    private Integer percent;
    private Integer initialQuantity;
    private boolean active;
 }
