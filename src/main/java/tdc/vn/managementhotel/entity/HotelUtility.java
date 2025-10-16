package tdc.vn.managementhotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelUtility extends BaseEntity {

    @EmbeddedId
    private HotelUtilityID id = new HotelUtilityID();
    @ManyToOne
    @MapsId("hotelId") // liên kết với trường studentId trong khóa chính tổng hợp
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
    @ManyToOne
    @MapsId("utilityId") // liên kết với trường studentId trong khóa chính tổng hợp
    @JoinColumn(name = "utility_id")
    private Utility utility;
    private BigDecimal price;
}
