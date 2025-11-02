package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.TypeOfRoomUtility;
import tdc.vn.managementhotel.enums.UtilityType;

import java.util.List;


@Repository
public interface TypeOfRoomUtilityRepository extends JpaRepository<TypeOfRoomUtility, Long> {
//    void deleteByHotel(Hotel hotel);
//
//    List<TypeOfRoomUtility> findByHotel_IdAndUtility_Type(Long hotelId, UtilityType utilityType);
    List<TypeOfRoomUtility> findByTypeOfRoom_IdAndUtility_TypeAndUtility_Hotel_Id(Long idTypeOfRoom, UtilityType utilityType, Long hotelId);
    List<TypeOfRoomUtility> findByUtility_Id(Long id);
    List<TypeOfRoomUtility> findByUtility_Hotel_IdAndUtility_Type(Long hotelId, UtilityType utilityType);

    List<TypeOfRoomUtility> findByUtility_Hotel_Id(Long hotelId);

}