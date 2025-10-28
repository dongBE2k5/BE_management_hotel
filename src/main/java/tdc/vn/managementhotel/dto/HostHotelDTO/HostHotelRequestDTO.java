package tdc.vn.managementhotel.dto.HostHotelDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostHotelRequestDTO {
    private Long userId;
    private String stk;
    private String nganHang;
    private String chiNhanh;
    private String cccd;
    private String cccdMatTruoc;
    private String cccdMatSau;
    private String giayPhepKinhDoanh;
}
