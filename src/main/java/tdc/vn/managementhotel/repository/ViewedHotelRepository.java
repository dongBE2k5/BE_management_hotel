package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.ViewedHotel;
import tdc.vn.managementhotel.entity.Hotel;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViewedHotelRepository extends JpaRepository<ViewedHotel, Long> {
    Optional<ViewedHotel> findByUserIdAndHotel(Long userId, Hotel hotel); // 👈 dùng entity Hotel
    List<ViewedHotel> findTop10ByUserIdOrderByViewedAtDesc(Long userId);

    @Query("""
SELECT h FROM Hotel h
JOIN ViewedHotel vh ON vh.hotel.id = h.id
WHERE vh.userId = :userId
AND (:locationId IS NULL OR h.location.id = :locationId)
ORDER BY vh.viewedAt DESC
""")
    List<Hotel> findRecentlyViewedByUserAndLocation(@Param("userId") Long userId, @Param("locationId") Long locationId);

}
