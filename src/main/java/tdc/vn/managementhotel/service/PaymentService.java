package tdc.vn.managementhotel.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.PaymentDTO.PaymentResponseDTO;
import tdc.vn.managementhotel.entity.Payment;
import tdc.vn.managementhotel.repository.PaymentRepository;

@Service
@RequiredArgsConstructor
public class PaymentService {


    private final PaymentRepository paymentRepository;

    public ResponseEntity<Payment> createPay(PaymentResponseDTO dto) {
        Payment payment = new Payment(
                null,                  // id tự động
                dto.getMethod(),
                dto.getTotal(),
                dto.getStatus(),
                dto.getBookingId(),
                dto.getTransactionStatus()
        );
       return ResponseEntity.ok( paymentRepository.save(payment));
    }
    public ResponseEntity<Payment> updatePay(PaymentResponseDTO dto) {
        Payment payment = paymentRepository.findPaymentByBookingId(dto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Payment not found for bookingId: " + dto.getBookingId()));
        payment.setStatus(dto.getStatus());
        return ResponseEntity.ok(paymentRepository.save(payment));
    }



}
