package tdc.vn.managementhotel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.ItemDTO.ItemRequestDTO;
import tdc.vn.managementhotel.dto.ItemDTO.ItemResponseDTO;
import tdc.vn.managementhotel.entity.Item;
import tdc.vn.managementhotel.repository.ItemRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    /**
     * Tạo một Item mới
     */
    public ItemResponseDTO createItem(ItemRequestDTO requestDTO) {
        // 1. Chuyển DTO thành Entity (dùng hàm helper mới)
        Item item = mapToNewEntity(requestDTO);

        // 2. Lưu vào cơ sở dữ liệu
        Item savedItem = itemRepository.save(item);

        // 3. Chuyển Entity đã lưu thành Response DTO để trả về
        return mapToResponseDTO(savedItem);
    }

    /**
     * Lấy một Item theo ID
     */
    public ItemResponseDTO getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        return mapToResponseDTO(item);
    }

    /**
     * Lấy tất cả các Item
     */
    public List<ItemResponseDTO> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Cập nhật một Item
     */
    public ItemResponseDTO updateItem(Long id, ItemRequestDTO requestDTO) {
        // 1. Tìm item
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        // 2. Cập nhật các trường (dùng hàm helper mới)
        updateEntityFromDTO(existingItem, requestDTO);

        // 3. Lưu lại
        Item updatedItem = itemRepository.save(existingItem);

        // 4. Trả về DTO
        return mapToResponseDTO(updatedItem);
    }

    /**
     * Xóa một Item
     */
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item not found with id: " + id);
        }
        itemRepository.deleteById(id);
    }


    // --- CÁC HÀM HELPER CHUYỂN ĐỔI ---

    /**
     * Hàm private để chuyển đổi từ Entity sang Response DTO
     */
    private ItemResponseDTO mapToResponseDTO(Item item) {
        ItemResponseDTO dto = new ItemResponseDTO();

        dto.setName(item.getName());

        return dto;
    }

    /**
     * Hàm private để tạo một Entity MỚI từ Request DTO
     */
    private Item mapToNewEntity(ItemRequestDTO requestDTO) {
        Item item = new Item();
        item.setName(requestDTO.getName());

        return item;
    }

    /**
     * Hàm private để cập nhật một Entity ĐÃ TỒN TẠI từ Request DTO
     */
    private void updateEntityFromDTO(Item item, ItemRequestDTO requestDTO) {
        item.setName(requestDTO.getName());

    }
}