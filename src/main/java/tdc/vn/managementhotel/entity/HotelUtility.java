package tdc.vn.managementhotel.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelUtility {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Long hotelId;
    private Long utilityId;
    private Long price;
}
