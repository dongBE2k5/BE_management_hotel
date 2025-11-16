package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.PaymentTypes;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentTypes, Long> {
}
