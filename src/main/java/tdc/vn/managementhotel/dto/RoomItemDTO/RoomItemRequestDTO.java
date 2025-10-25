package tdc.vn.managementhotel.dto.RoomItemDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomItemRequestDTO {
    private Long typeOfRoomId;

    // Khi thêm 1 item
    private Long itemId;
    private Integer quantity;
    private BigDecimal price;

    // Khi thêm nhiều item
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

    // Hàm helper để kiểm tra loại request
    public boolean isBatchRequest() {
        return items != null && !items.isEmpty();
    }
}