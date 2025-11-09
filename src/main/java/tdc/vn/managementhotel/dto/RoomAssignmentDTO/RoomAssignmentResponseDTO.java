package tdc.vn.managementhotel.dto.RoomAssignmentDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.AssignmentStatus;

import java.time.LocalDateTime;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomAssignmentResponseDTO {
    private Long id;

    private Long roomId;
    private String roomNumber;
    private String roomType;

    private Long employeeId;
    private String employeeName;

    private Long assignedById;
    private String assignedByName;

    private AssignmentStatus status;
    private String note;

    private Long requestId;

    private LocalDateTime assignedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime completedAt;

    private Long bookingId;
}