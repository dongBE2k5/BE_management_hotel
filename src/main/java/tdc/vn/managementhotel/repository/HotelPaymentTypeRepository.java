package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.HotelPaymentType;

import java.util.List;

@Repository
public interface HotelPaymentTypeRepository extends JpaRepository<HotelPaymentType, Long> {
    List<HotelPaymentType> findByHotelId(Long hotelId);
    List<HotelPaymentType> findByHotelIdAndTypeOfRoom_Id(Long hotelId, Long roomId);

    boolean existsByHotelIdAndPaymentTypeIdAndTypeOfRoom_Id(Long hotelId, Long paymentTypeId, Long typeOfRoomId);

    List<HotelPaymentType> findByHotelIdAndPaymentTypeId(Long hotelId, Long paymentTypeId);

    List<HotelPaymentType> findByIdOrHotelIdAndTypeOfRoomId(Long id, Long hotelId, Long typeOfRoomId);
    List<HotelPaymentType> findByHotelIdAndTypeOfRoomId(Long hotelId, Long typeOfRoomId);

}
