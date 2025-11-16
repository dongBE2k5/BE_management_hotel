package tdc.vn.managementhotel.dto.BookingDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.PaymentMethod;
import tdc.vn.managementhotel.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long roomId;
    private Long userId;
    private List<Long> voucherIds;
    private BigDecimal totalPrice;
    private BigDecimal paidPrice;
    private Long hotelPaymentTypeId;
    private PaymentMethod paymentMethod;
}
