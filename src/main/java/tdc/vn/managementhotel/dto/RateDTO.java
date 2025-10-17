package tdc.vn.managementhotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateDTO {
    private Long id;
    private int rateNumber;
    private List<String> likedPoints;
    private String comment;// ✅
    private Long roomId;    // ✅
    private Long userId;
    private String username;
}
