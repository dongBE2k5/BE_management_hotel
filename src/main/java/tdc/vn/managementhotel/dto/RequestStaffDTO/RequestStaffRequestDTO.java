package tdc.vn.managementhotel.dto.RequestStaffDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.RequestStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestStaffRequestDTO {
    private Long senderId;
    private Long receiverId;
    private String content;

    // Có thể cho phép client gửi status nếu cần (ví dụ để cập nhật)
    private RequestStatus status;
}
