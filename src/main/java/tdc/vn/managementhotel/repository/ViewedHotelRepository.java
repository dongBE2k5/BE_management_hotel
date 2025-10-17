package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.ViewedHotel;
import tdc.vn.managementhotel.entity.Hotel;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViewedHotelRepository extends JpaRepository<ViewedHotel, Long> {
    Optional<ViewedHotel> findByUserIdAndHotel(Long userId, Hotel hotel); // 👈 dùng entity Hotel
    List<ViewedHotel> findTop10ByUserIdOrderByViewedAtDesc(Long userId);
}
