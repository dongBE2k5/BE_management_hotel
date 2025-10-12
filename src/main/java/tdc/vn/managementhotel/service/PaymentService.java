package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.PaymentDTO.PaymentResponseDTO;
import tdc.vn.managementhotel.entity.Payment;
import tdc.vn.managementhotel.repository.PaymentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    // 🟢 Tạo mới thanh toán
    public PaymentResponseDTO createPay(PaymentResponseDTO dto) {
        Payment payment = new Payment(
                null, // id tự động
                dto.getMethod(),
                dto.getTotal(),
                dto.getStatus(),
                dto.getBookingId(),
                dto.getTransactionStatus()
        );
        return mapEntityToResponse(paymentRepository.save(payment));
    }

    // 🟢 Cập nhật thanh toán theo bookingId
    public PaymentResponseDTO updatePay(PaymentResponseDTO dto) {
        Payment payment = paymentRepository.findPaymentByBookingId(dto.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Payment not found for bookingId: " + dto.getBookingId()));

        payment.setStatus(dto.getStatus());
        payment.setTransactionStatus(dto.getTransactionStatus());
        return mapEntityToResponse(paymentRepository.save(payment));
    }

    // 🟢 Lấy thanh toán theo ID
    public PaymentResponseDTO getById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
        return mapEntityToResponse(payment);
    }

    // 🟢 Lấy danh sách thanh toán theo BookingId
    public List<PaymentResponseDTO> getByBookingId(Long bookingId) {
        List<Payment> payments = paymentRepository.findAllByBookingId(bookingId);
        if (payments.isEmpty()) {
            throw new EntityNotFoundException("No payments found for bookingId: " + bookingId);
        }
        return payments.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    // 🟢 Lấy toàn bộ payment
    public List<PaymentResponseDTO> getAll() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    // 🔁 Map Entity → DTO
    private PaymentResponseDTO mapEntityToResponse(Payment payment) {
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getMethod(),
                payment.getTotal(),
                payment.getStatus(),
                payment.getBookingId(),
                payment.getTransactionStatus()
        );
    }
}
