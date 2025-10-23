package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.ImageRoom;
import tdc.vn.managementhotel.entity.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRoomRepository extends JpaRepository<ImageRoom, Long> {
//    List<ImageRoom> findByRoomId(Long id);
    List<ImageRoom> findAllByHotelId(Long hotelId);
    List<ImageRoom> findByHotelIdAndRoomTypeId(Long hotelId, Long roomTypeId);
    List<ImageRoom> findByHotelId(Long hotelId);
    // (Tùy chọn) Nếu muốn lọc theo loại phòng trong khách sạn
    List<ImageRoom> findAllByHotelIdAndRoomType_Id(Long hotelId, Long roomTypeId);

    void deleteByHotel_Id(Long hotelId);
    void deleteByHotel(Hotel hotel);
    void deleteByHotelId(Long hotelId);
    void deleteByHotelIdAndRoomType_Id(Long hotelId, Long roomTypeId);
    
}

