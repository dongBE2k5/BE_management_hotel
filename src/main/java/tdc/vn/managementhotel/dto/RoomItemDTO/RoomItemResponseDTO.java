package tdc.vn.managementhotel.dto.RoomItemDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomItemResponseDTO {
    private Long typeOfRoomId;
    private String typeOfRoomName;

    // Nếu chỉ có 1 item
    private Long itemId;
    private String itemName;
    private Integer quantity;
    private BigDecimal price;

    // Nếu có nhiều item
    private List<ItemDetail> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemDetail {
        private Long itemId;
        private String itemName;
        private int quantity;
        private BigDecimal price;
    }

    public boolean isBatchResponse() {
        return items != null && !items.isEmpty();
    }
}

