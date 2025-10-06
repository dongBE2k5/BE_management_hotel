package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tdc.vn.managementhotel.entity.ImageRoom;
import tdc.vn.managementhotel.entity.Role;

import java.util.List;
import java.util.Optional;

public interface ImageRoomRepository extends JpaRepository<ImageRoom, Long> {
//    List<ImageRoom> findByRoomId(Long id);

    List<ImageRoom> findByHotelIdAndRoomTypeId(Long hotelId, Long roomTypeId);
    List<ImageRoom> findByHotelId(Long hotelId);
}

