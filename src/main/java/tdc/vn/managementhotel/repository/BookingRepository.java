package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tdc.vn.managementhotel.entity.Booking;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.enums.BookingStatus;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByUser_IdAndRoom_IdAndStatus(Long userId, Long roomId, BookingStatus status);
    List<Booking> findByRoom_Hotel_Id(Long hotelId);
    List<Booking> findByUser_Id(Long userId);

    @Modifying
    @Transactional
    @Query("""
    UPDATE Booking b
    SET b.status = tdc.vn.managementhotel.enums.BookingStatus.DA_HUY
    WHERE b.status = tdc.vn.managementhotel.enums.BookingStatus.CHUA_THANH_TOAN
    AND b.createdAt <= :time
""")
    void updateExpiredBookings(@Param("time") LocalDateTime time);

    //tinh booking bestchoice 5 ks cao nhat
    @Query("""
        SELECT b.room.hotel AS hotel, COUNT(b) AS totalBookings
        FROM Booking b
        WHERE b.status = tdc.vn.managementhotel.enums.BookingStatus.DA_THANH_TOAN
        GROUP BY b.room.hotel
        ORDER BY COUNT(b) DESC
    """)
    List<Object[]> findTop5HotelsWithMostBookings(Pageable pageable);

    // ✅ Top 5 khách sạn theo location
    @Query("""
        SELECT b.room.hotel AS hotel, COUNT(b) AS totalBookings
        FROM Booking b
        WHERE b.status = tdc.vn.managementhotel.enums.BookingStatus.DA_THANH_TOAN
          AND b.room.hotel.location.id = :locationId
        GROUP BY b.room.hotel
        ORDER BY COUNT(b) DESC
    """)
    List<Object[]> findTop5HotelsWithMostBookingsByLocation(@Param("locationId") Long locationId, Pageable pageable);
}