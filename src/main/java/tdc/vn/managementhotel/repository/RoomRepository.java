package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.Room;
import tdc.vn.managementhotel.enums.StatusRoom;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {


    @Query("SELECT r.price FROM Room r WHERE r.hotel.id = :hotelId")
    List<BigDecimal> findPricesByHotelId(@Param("hotelId") Long hotelId);


    List<Room> findByHotelId(Long hotelId);
    @Query(value = """
    SELECT r.* 
    FROM room r
    WHERE r.hotel_id = :hotelId and r.status = :status
      AND r.id NOT IN (
          SELECT b.room_id 
          FROM bookings b
          WHERE b.check_in_date < :checkOutDate
            AND b.check_out_date > :checkInDate
      )
    """, nativeQuery = true)
    List<Room> findAvailableRooms(
            @Param("hotelId") Long hotelId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("status") String status
    );

    @Query(value = """
    SELECT 
        MIN(price) AS minPrice, 
        MAX(price) AS maxPrice 
    FROM room 
    WHERE hotel_id = :hotelId
""", nativeQuery = true)
    Map<String, BigDecimal> findPriceRangeByHotelId(@Param("hotelId") Long hotelId);

    @Query(value = """
    SELECT r.*
        FROM room r
        WHERE status = :status
          AND r.id IN (
              SELECT b.room_id
              FROM bookings b
              WHERE b.check_in_date = :checkInDate
          )
    """, nativeQuery = true)
    List<Room> findRoomsToSchedule(
//            @Param("hotelId") Long hotelId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("status") String status
    );
}
