package tdc.vn.managementhotel.dto.PaymentDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.NonFinal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {

    private Long id;
    private String method;
    private Long total;
    private String status;
    private Long bookingId;
    private String transactionStatus;
}
