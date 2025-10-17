package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tdc.vn.managementhotel.entity.Rate;
import java.util.List;
import java.util.Optional;

public interface RateRepository extends JpaRepository<Rate, Long> {
    @Query("SELECT r FROM Rate r WHERE r.room.hotel.id = :hotelId")
    List<Rate> findByHotelId(@Param("hotelId") Long hotelId);


    Optional<Rate> findByUser_IdAndRoom_Id(Long userId, Long roomId);
}
