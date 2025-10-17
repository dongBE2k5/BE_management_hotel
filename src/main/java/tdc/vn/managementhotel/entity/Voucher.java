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
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 👈 Thêm trường này

    private String code;
    private String description;
    private Long priceCondition;
    private Long hotelId;
    private Integer quantity; // Tổng số voucher
    private Integer percent;
    private Integer used = 0; // Số voucher đã dùng
}
