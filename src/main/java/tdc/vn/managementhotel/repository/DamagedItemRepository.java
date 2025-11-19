package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.Booking;
import tdc.vn.managementhotel.entity.DamagedItem;
import tdc.vn.managementhotel.entity.RequestStaff;
import tdc.vn.managementhotel.entity.Room;
import tdc.vn.managementhotel.enums.DamageStatus;

import java.util.List;


@Repository
public interface DamagedItemRepository extends JpaRepository<DamagedItem, Long> {
    List<DamagedItem> findByStatus(DamageStatus status);
    List<DamagedItem> findByRoom(Room room);
    List<DamagedItem> findByRequestStaff(RequestStaff requestStaff);
    List<DamagedItem> findByBooking(Booking booking);
    List<DamagedItem> findByBookingId(Long booking);
}
