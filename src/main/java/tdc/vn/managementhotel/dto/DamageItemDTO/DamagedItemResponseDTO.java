package tdc.vn.managementhotel.dto.DamageItemDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.DamageStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DamagedItemResponseDTO {
    private Long requestStaffId;
    private Long id;
    private Long roomId;
    private String roomNumber;
    private Long itemId;
    private String itemName;
    private int quantityAffected;
    private DamageStatus status;
    private String image;
    private String reportedBy;
    private LocalDateTime reportedAt;
}
