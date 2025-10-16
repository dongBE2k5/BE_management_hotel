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
public class HotelUtilityID implements Serializable {
    private Long hotelId;
    private Long utilityId;
}
