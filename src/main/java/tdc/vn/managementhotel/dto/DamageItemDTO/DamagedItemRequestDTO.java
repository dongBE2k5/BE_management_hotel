package tdc.vn.managementhotel.dto.DamageItemDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.entity.User;
import tdc.vn.managementhotel.enums.DamageStatus;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DamagedItemRequestDTO {
    private Long roomId;
    private Long itemId;
    private int quantityAffected;
    private DamageStatus status;
    private String image;
    private Long reportedBy;
    private Long requestStaffId;
}