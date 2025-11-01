package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request,Long> {
    List<Request> findByReceiverId(Long receiverId);

    List<Request> findBySenderId(Long senderId);

    Optional<Request> findAllById(Long id);
}
