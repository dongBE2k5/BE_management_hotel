package tdc.vn.managementhotel.dto.HotelDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewedHotelRequestDTO {
    private Long userId;
    private Long hotelId;
}