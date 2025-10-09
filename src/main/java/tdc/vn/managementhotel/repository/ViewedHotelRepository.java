package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tdc.vn.managementhotel.entity.ViewedHotel;
import java.util.List;
import java.util.Optional;

public interface ViewedHotelRepository extends JpaRepository<ViewedHotel, Long> {
    Optional<ViewedHotel> findByUserIdAndHotelId(Long userId, Long hotelId);
    List<ViewedHotel> findTop10ByUserIdOrderByViewedAtDesc(Long userId);
}
