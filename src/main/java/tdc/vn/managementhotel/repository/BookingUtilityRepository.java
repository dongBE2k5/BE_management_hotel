package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.BookingUtility;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.HotelUtility;
import tdc.vn.managementhotel.enums.UtilityType;

import java.util.List;

@Repository
public interface BookingUtilityRepository extends JpaRepository<BookingUtility, Long> {
    List<BookingUtility> findByBooking_Id(Long id);
}
