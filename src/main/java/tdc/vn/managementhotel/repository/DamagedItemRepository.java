package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tdc.vn.managementhotel.entity.DamagedItem;
import tdc.vn.managementhotel.entity.Room;
import tdc.vn.managementhotel.enums.DamageStatus;

import java.util.List;

public interface DamagedItemRepository extends JpaRepository<DamagedItem, Long> {
    List<DamagedItem> findByStatus(DamageStatus status);
    List<DamagedItem> findByRoom(Room room);
}
