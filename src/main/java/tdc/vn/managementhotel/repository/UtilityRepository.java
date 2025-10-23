package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.Utility;
import tdc.vn.managementhotel.enums.UtilityType;

import java.util.List;

@Repository
public interface UtilityRepository extends JpaRepository<Utility, Long> {
//    List<Utility> findBy(Long clientId);
    List<Utility> findByType(UtilityType type);

//    List<Utility> findByType(UtilityType type);
}
