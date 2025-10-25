package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tdc.vn.managementhotel.entity.Item;

public interface ItemRepository extends JpaRepository<Item,Long> {


}
