package tdc.vn.managementhotel.dto.UtilityDTO;

import java.math.BigDecimal;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import tdc.vn.managementhotel.enums.UtilityType;
import tdc.vn.managementhotel.enums.UtilityUsed;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilityDTO {
    private Long id;
    private String name;
    private String imageUrl;
    private UtilityType type;
    private Long hotelId;
    private BigDecimal price;
    private UtilityUsed isUsed;
}
