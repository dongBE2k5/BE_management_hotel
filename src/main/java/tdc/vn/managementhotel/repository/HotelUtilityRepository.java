package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.HotelUtility;
import tdc.vn.managementhotel.enums.UtilityType;

import java.util.List;


@Repository
public interface HotelUtilityRepository extends JpaRepository<HotelUtility, Long> {
    void deleteByHotel(Hotel hotel);
    List<HotelUtility> findByHotel_Id(Long id);

    List<HotelUtility> findByHotel_IdAndUtility_Type(Long hotelId, UtilityType utilityType);
}