package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {


}
