package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdc.vn.managementhotel.entity.PaymentTypes;
import tdc.vn.managementhotel.entity.Role;
import tdc.vn.managementhotel.enums.PaymentType;

import java.util.Optional;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentTypes, Long> {
    Optional<PaymentTypes> findByPaymentType(PaymentType paymentType);
}
