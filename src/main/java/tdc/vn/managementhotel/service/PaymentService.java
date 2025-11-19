package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.PaymentDTO.PaymentResponseDTO;
import tdc.vn.managementhotel.entity.Booking;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.Payment;
import tdc.vn.managementhotel.enums.PaymentMethod;
import tdc.vn.managementhotel.enums.PaymentStatus;
import tdc.vn.managementhotel.repository.BookingRepository;
import tdc.vn.managementhotel.repository.HotelRepository;
import tdc.vn.managementhotel.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;

    // 🟢 Tạo mới thanh toán
    public PaymentResponseDTO createPay(PaymentResponseDTO dto) {
        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        Payment payment = new Payment(
                null, // id tự động
                PaymentMethod.valueOf(dto.getMethod()),
                dto.getTotal(),
                PaymentStatus.valueOf(dto.getStatus()),
                booking,
                dto.getTransactionStatus(),
                LocalDateTime.now()
        );
        return mapEntityToResponse(paymentRepository.save(payment));
    }

    // 🟢 Cập nhật thanh toán theo bookingId
    public PaymentResponseDTO updatePay(PaymentResponseDTO dto) {
        Payment payment = paymentRepository.findPaymentByBookingId(dto.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Payment not found for bookingId: " + dto.getBookingId()));

        payment.setStatus(PaymentStatus.valueOf(dto.getStatus()));
        payment.setTransactionStatus(dto.getTransactionStatus());
        return mapEntityToResponse(paymentRepository.save(payment));
    }

    // 🟢 Cập nhật thanh toán theo id
    public PaymentResponseDTO updatePayById(Long Id, String paymentStatus) {
        Payment payment = paymentRepository.findPaymentById((Id)).orElseThrow(() -> new EntityNotFoundException("Payment not found for id: " + Id));

        payment.setStatus(PaymentStatus.valueOf(paymentStatus));
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

    // 🟢 Lấy danh sách thanh toán theo HotelId
    public List<PaymentResponseDTO> getByHotel(Long hotelId) {
        // Check hotel
        hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found for id: " + hotelId));

        // Lấy tất cả booking theo hotelId
        List<Booking> bookingList =
                bookingRepository.findByRoom_Hotel_Id(hotelId);

        // Mapping sang PaymentResponseDTO
        return bookingList.stream()
                .flatMap(booking -> {
                    List<Payment> payments = paymentRepository.findAllByBookingId(booking.getId());

                    if (payments.isEmpty()) {
                        // Nếu muốn bỏ qua booking không có thanh toán => return Stream.empty();
                        throw new EntityNotFoundException("No payments found for bookingId: " + booking.getId());
                    }

                    return payments.stream().map(this::mapEntityToResponse);
                })
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
            String.valueOf(payment.getMethod()),
            payment.getTotal(),
            String.valueOf(payment.getStatus()),
            payment.getBooking().getId(),
            payment.getTransactionStatus()
    );
}
}
