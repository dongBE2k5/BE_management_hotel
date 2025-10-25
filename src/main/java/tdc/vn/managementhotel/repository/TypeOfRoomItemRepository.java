package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.Item;
import tdc.vn.managementhotel.entity.TypeOfRoom;
import tdc.vn.managementhotel.entity.TypeOfRoomItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeOfRoomItemRepository extends JpaRepository<TypeOfRoomItem, Long> {
    List<TypeOfRoomItem> findByTypeOfRoom(TypeOfRoom typeOfRoom);
    Optional<TypeOfRoomItem> findByTypeOfRoomAndItem(TypeOfRoom typeOfRoom, Item item);
}
