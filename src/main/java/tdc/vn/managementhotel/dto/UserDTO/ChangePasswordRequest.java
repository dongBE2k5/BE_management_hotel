package tdc.vn.managementhotel.dto.UserDTO;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
