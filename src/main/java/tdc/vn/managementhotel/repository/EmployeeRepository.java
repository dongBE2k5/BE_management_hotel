package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.Employee;

import java.util.List;
import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllByHotelId(Long hotelId);


    Optional<Employee> findByUserId(Long userId );

    Employee findEmployeeByUserId(Long userId);
}
