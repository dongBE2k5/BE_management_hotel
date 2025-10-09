package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.Room;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);
    @Query(value = """
    SELECT r.* 
    FROM room r
    WHERE r.hotel_id = :hotelId 
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
            @Param("checkOutDate") LocalDate checkOutDate
    );


}
