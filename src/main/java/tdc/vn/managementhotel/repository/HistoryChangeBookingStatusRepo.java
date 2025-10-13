package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.HistoryChangeBookingStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryChangeBookingStatusRepo extends JpaRepository<HistoryChangeBookingStatus, Long> {
        List<HistoryChangeBookingStatus> findByBooking_Id(Long bookingId);

}
