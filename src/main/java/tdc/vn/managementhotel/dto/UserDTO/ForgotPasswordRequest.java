package tdc.vn.managementhotel.dto.UserDTO;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
    private String newPassword;
    private String otp;
}
