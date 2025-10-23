package tdc.vn.managementhotel.dto.UtilityDTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.UtilityType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilityDTO {
    private Long id;
    private String name;
    private String image;
    private UtilityType type;
}
