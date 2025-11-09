package tdc.vn.managementhotel.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.EmployeeDTO.EmployeeResponseDTO;
import tdc.vn.managementhotel.dto.EmployeeDTO.EmployeeResquestDTO;
import tdc.vn.managementhotel.dto.UserDTO.UserResponse;
import tdc.vn.managementhotel.entity.Employee;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.User;
import tdc.vn.managementhotel.repository.EmployeeRepository;
import tdc.vn.managementhotel.repository.HotelRepository;
import tdc.vn.managementhotel.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;

    //    created
    public EmployeeResponseDTO createEmployee(EmployeeResquestDTO employeeResquestDTO) {
        Employee employee = new Employee();
        mapDtoToEntity(employeeResquestDTO, employee);
        return mapEntityToResponse(employeeRepository.save(employee));
    }

    //    update
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeResquestDTO employeeResquestDTO) {
        Employee employee = employeeRepository.findEmployeeByUserId((id))
                .orElseThrow(() -> new RuntimeException("Employee not found"));
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
    private void mapDtoToEntity(EmployeeResquestDTO dto, Employee employee) {
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            employee.setUser(user);
        }

        if (dto.getHotelId() != null) {
            Hotel hotel = hotelRepository.findById(dto.getHotelId())
                    .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
            employee.setHotel(hotel);
        }

        employee.setPosition(dto.getPosition());
    }

    ;

    // Map Entity → Response DTO
    private EmployeeResponseDTO mapEntityToResponse(Employee employee) {
            User user = employee.getUser();
        return new EmployeeResponseDTO(
                new UserResponse(user.getId(),user.getFullName(),user.getUsername(),user.getEmail(),user.getPhone(),user.getCccd(),user.getRole(),user.getGender(),user.getBirthDate(),user.getAddress()),
                employee.getHotel().getId(),
                employee.getPosition().toString()
        );
    }

}
