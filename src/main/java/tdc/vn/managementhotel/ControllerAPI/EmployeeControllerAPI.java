package tdc.vn.managementhotel.controllerAPI;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.EmployeeDTO.EmployeeResponseDTO;
import tdc.vn.managementhotel.dto.EmployeeDTO.EmployeeResquestDTO;
import tdc.vn.managementhotel.entity.Employee;
import tdc.vn.managementhotel.service.EmployeeService;
import tdc.vn.managementhotel.service.HotelService;
import tdc.vn.managementhotel.service.UserService;

import java.util.List;

@RestController
@RequestMapping("api/employee")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class EmployeeControllerAPI {
    private final UserService userServicel;
    private final EmployeeService employeeService;
    private final HotelService hotelService ;

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@RequestBody EmployeeResquestDTO employeeResquestDTO) {
        return ResponseEntity.ok().body(employeeService.createEmployee(employeeResquestDTO));
    }

    @GetMapping("{hotelID}/hotel")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeeByHotelId(@PathVariable Long hotelID) {
        return  ResponseEntity.ok(employeeService.findEmployeeByHotelId(hotelID));
    }
    @GetMapping("{userID}/user")
    public  ResponseEntity<EmployeeResponseDTO> getEmployeeByUserId(@PathVariable Long userID) {
        return  ResponseEntity.ok(employeeService.findEmployeeByUserId(userID));

    }






}
