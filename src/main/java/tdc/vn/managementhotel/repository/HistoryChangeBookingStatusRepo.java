package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.HistoryChangeBookingStatus;

@Repository
public interface HistoryChangeBookingStatusRepo extends JpaRepository<HistoryChangeBookingStatus, Long> {
}
