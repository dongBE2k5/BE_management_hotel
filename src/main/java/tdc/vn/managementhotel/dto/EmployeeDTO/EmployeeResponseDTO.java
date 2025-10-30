package tdc.vn.managementhotel.dto.EmployeeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.dto.UserDTO.UserResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private UserResponse user;
    private Long hotelId;
    private String position;
}
    