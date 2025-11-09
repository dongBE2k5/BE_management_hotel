package tdc.vn.managementhotel.dto.HotelPaymentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.PaymentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelPaymentTypeResponseDTO {
    private Long id;
    private Long hotelId;
    private PaymentType paymentType;
    private Double depositPercent;
}
