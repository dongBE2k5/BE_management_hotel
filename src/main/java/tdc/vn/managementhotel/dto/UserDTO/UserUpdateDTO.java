package tdc.vn.managementhotel.dto.UserDTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDTO {
    private String fullName;
    private String email;
    private String phone;
    private String cccd;
    private String gender;
    private LocalDate birthDate;
    private String address;
}