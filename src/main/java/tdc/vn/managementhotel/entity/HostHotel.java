package tdc.vn.managementhotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.HostHotelStatus;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostHotel {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String stk;
    private String nganHang;
    private String chiNhanh;
    private String cccd;
    private String cccdMatTruoc;
    private String cccdMatSau;
    private String giayPhepKinhDoanh;
    private HostHotelStatus status;

}
