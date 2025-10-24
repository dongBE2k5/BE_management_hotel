package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.Role;
import tdc.vn.managementhotel.entity.TypeOfRoom;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeOfRoomRepository extends JpaRepository<TypeOfRoom, Long> {
//    List<TypeOfRoom> findByHotel_Id(Long id);
}

