package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tdc.vn.managementhotel.entity.Location;
import tdc.vn.managementhotel.entity.Role;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
