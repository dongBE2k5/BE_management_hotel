package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.HostHotel;

import java.util.Optional;

@Repository
public interface HostHotelRepository  extends JpaRepository<HostHotel,Long> {

    Optional<HostHotel> findByUserId(Long userId);
}
