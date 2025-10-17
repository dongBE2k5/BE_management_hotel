package tdc.vn.managementhotel.entity;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class BookingUtilityID implements Serializable {
    private Long bookingId;
    private Long utilityId;
}