package tdc.vn.managementhotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import tdc.vn.managementhotel.entity.Role;

@Data
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private Long id;
    private String username;
    private Role role;
    private String accessToken;
}