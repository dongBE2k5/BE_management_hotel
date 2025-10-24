package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.SavedHotel;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedHotelRepository extends JpaRepository<SavedHotel, Long> {
    List<SavedHotel> findByUserId(Long userId);
    Optional<SavedHotel> findByUserIdAndHotelId(Long userId, Long hotelId);
    //xóa
    void deleteByUserIdAndHotelId(Long userId, Long hotelId);

    //  Lấy danh sách SavedHotel theo userId + locationId
    @Query("SELECT s FROM SavedHotel s " +
            "WHERE s.userId = :userId AND s.hotelId IN (" +
            "    SELECT h.id FROM Hotel h WHERE h.location.id = :locationId" +
            ")")
    List<SavedHotel> findByUserIdAndLocationId(Long userId, Long locationId);
}
