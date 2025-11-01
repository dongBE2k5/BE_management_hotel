package tdc.vn.managementhotel.dto.RoomAssignmentDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.AssignmentStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAssignmentRequestDTO {
    private Long roomId;        // ID của phòng được phân công
    private Long employeeId;    // Nhân viên được giao
    private Long assignedById;  // Người giao nhiệm vụ (quản lý hoặc lễ tân)
    private Long requestId;
    private String note;        // Ghi chú thêm (nếu có)
}

