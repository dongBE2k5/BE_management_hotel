package tdc.vn.managementhotel.entity;

import jakarta.persistence.*;
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
    @Column(name = "used", nullable = false)
    private Integer used = 0;
    private int initialQuantity;
    private boolean active = true;
}
