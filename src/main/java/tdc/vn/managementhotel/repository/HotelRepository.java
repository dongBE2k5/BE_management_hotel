package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tdc.vn.managementhotel.entity.Hotel;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByLocationId(Long locationId);
    List<Hotel> findByUserId(Long userId);

}
