package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tdc.vn.managementhotel.entity.Role;
import tdc.vn.managementhotel.entity.TypeOfRoom;

import java.util.List;
import java.util.Optional;

public interface TypeOfRoomRepository extends JpaRepository<TypeOfRoom, Long> {
}

