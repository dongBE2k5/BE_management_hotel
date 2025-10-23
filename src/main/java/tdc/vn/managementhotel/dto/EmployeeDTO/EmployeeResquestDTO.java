package tdc.vn.managementhotel.dto.EmployeeDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResquestDTO {
    private Long id;
    private User userId;
    private Hotel hotelId;
    private String position;

}
