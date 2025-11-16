package tdc.vn.managementhotel.dto.HotelPaymentTypeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.PaymentType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelPaymentTypeRequestDTO {
    private Long hotelId;
    private Long paymentTypeId;
    private Double depositPercent;
    private List<Long> roomTypeIds;}
