package tdc.vn.managementhotel.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tdc.vn.managementhotel.entity.Employee;
import tdc.vn.managementhotel.entity.RoomAssignment;

import java.util.List;

public interface RoomAssignmentRepository extends JpaRepository<RoomAssignment, Long> {
    List<RoomAssignment> findByEmployee(Employee employee);
    List<RoomAssignment> findByStatus(String status);
    List<RoomAssignment> findByEmployeeId(Long employeeId);
}
