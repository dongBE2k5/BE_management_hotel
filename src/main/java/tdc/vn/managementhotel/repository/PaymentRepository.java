package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tdc.vn.managementhotel.entity.Payment;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional <Payment> findPaymentByBookingId(Long bookingId);
}
