package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.RequestStaff;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestStaffRepository extends JpaRepository<RequestStaff,Long> {
    List<RequestStaff> findByReceiverId(Long receiverId);

    List<RequestStaff> findBySenderId(Long senderId);

    Optional<RequestStaff> findAllById(Long id);
}
