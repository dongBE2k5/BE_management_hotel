package tdc.vn.managementhotel.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.EmployeeDTO.EmployeeResponseDTO;
import tdc.vn.managementhotel.dto.EmployeeDTO.EmployeeResquestDTO;
import tdc.vn.managementhotel.entity.Employee;
import tdc.vn.managementhotel.repository.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    //    created
    public EmployeeResponseDTO createEmployee(EmployeeResquestDTO employeeResquestDTO) {
        Employee employee = new Employee();
        mapDtoToEntity(employeeResquestDTO, employee);
        return mapEntityToResponse(employeeRepository.save(employee));
    }

    //    update
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeResquestDTO employeeResquestDTO) {
        Employee employee = employeeRepository.findEmployeeByUserId((id));
        mapDtoToEntity(employeeResquestDTO, employee);
        return mapEntityToResponse(employeeRepository.save(employee));
    }

    //    delete
    public void deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee not found");
        }
        employeeRepository.deleteById(id);
    }

    //    get by hotelid
    public List<EmployeeResponseDTO> findEmployeeByHotelId(Long hotelId) {
        return employeeRepository.findAllByHotelId(hotelId)
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    //    get by userid
    public EmployeeResponseDTO findEmployeeByUserId(Long userId) {
        return employeeRepository.findByUserId(userId)
                .map(this::mapEntityToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }


    // Map ResquestDTO → Entity
    private void mapDtoToEntity(EmployeeResquestDTO employeeResquestDTO, Employee employee) {
        employee.setId(employeeResquestDTO.getId());
        employee.setUser(employeeResquestDTO.getUserId());
        employee.setHotel(employeeResquestDTO.getHotelId());
        employee.setPosition(employeeResquestDTO.getPosition());
    }

    ;

    // Map Entity → Response DTO
    private EmployeeResponseDTO mapEntityToResponse(Employee employee) {
        return new EmployeeResponseDTO(employee.getHotel().getId(),
                employee.getPosition()
        );
    }

}
