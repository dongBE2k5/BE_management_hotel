//package tdc.vn.managementhotel.service;
//
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import tdc.vn.managementhotel.dto.RoomItemDTO.RoomItemRequestDTO;
//import tdc.vn.managementhotel.dto.RoomItemDTO.RoomItemResponseDTO;
//import tdc.vn.managementhotel.entity.Item;
//import tdc.vn.managementhotel.entity.TypeOfRoom;
//import tdc.vn.managementhotel.entity.TypeOfRoomItem;
//import tdc.vn.managementhotel.repository.ItemRepository;
//import tdc.vn.managementhotel.repository.TypeOfRoomRepository;
//// Bạn sẽ cần repository cho TypeOfRoomItem
//import tdc.vn.managementhotel.repository.TypeOfRoomItemRepository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class TypeOfRoomItemService {
//
//    private final TypeOfRoomRepository typeOfRoomRepository;
//    private final ItemRepository itemRepository;
//    // Không bắt buộc, nhưng hữu ích nếu bạn muốn
//    private final TypeOfRoomItemRepository typeOfRoomItemRepository;
//
//    /**
//     * Lấy tất cả Item (và số lượng) thuộc về một TypeOfRoom
//     */
//    @Transactional(readOnly = true)
//    public List<RoomItemResponseDTO> getItemsForRoom(Long roomId) {
//        TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(roomId)
//                .orElseThrow(() -> new EntityNotFoundException("TypeOfRoom not found with id: " + roomId));
//
//        // Lấy danh sách từ bảng trung gian
//        return typeOfRoom.getTypeOfRoomItems().stream()
//                .map(this::mapJoinEntityToResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Cập nhật (thay thế) toàn bộ danh sách Item và số lượng của chúng cho một TypeOfRoom
//     */
//    @Transactional
//    public List<RoomItemResponseDTO> updateItemsForRoom(Long roomId, List<RoomItemRequestDTO> itemRequests) {
//        // 1. Tìm TypeOfRoom
//        TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(roomId)
//                .orElseThrow(() -> new EntityNotFoundException("TypeOfRoom not found with id: " + roomId));
//
//        // 2. Xóa tất cả các liên kết cũ
//        // Vì có orphanRemoval=true, hành động này sẽ xóa các bản ghi trong TypeOfRoomItem
//        typeOfRoom.getTypeOfRoomItems().clear();
//
//        // 3. Tạo và thêm các liên kết mới
//        for (RoomItemRequestDTO req : itemRequests) {
//            Item item = itemRepository.findById(req.getItemId())
//                    .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + req.getItemId()));
//
//            // Tạo một bản ghi bảng trung gian mới
//            TypeOfRoomItem newJoinEntity = new TypeOfRoomItem();
//            newJoinEntity.setTypeOfRoom(typeOfRoom);
//            newJoinEntity.setItem(item);
//            newJoinEntity.setQuantity(req.getQuantity());
//
//            // Thêm bản ghi mới vào Set
//            typeOfRoom.getTypeOfRoomItems().add(newJoinEntity);
//        }
//
//        // 4. Lưu TypeOfRoom (để cascade lưu các bản ghi TypeOfRoomItem mới)
//        TypeOfRoom updatedRoom = typeOfRoomRepository.save(typeOfRoom);
//
//        // 5. Trả về DTO
//        return updatedRoom.getTypeOfRoomItems().stream()
//                .map(this::mapJoinEntityToResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//    // --- Hàm Helper ---
//
//    /**
//     * Chuyển đổi từ Bảng trung gian (Join Entity) sang DTO
//     */
//    private RoomItemResponseDTO mapJoinEntityToResponseDTO(TypeOfRoomItem joinEntity) {
//        return new RoomItemResponseDTO(
//                joinEntity.getItem().getId(),
//                joinEntity.getItem().getName(),
//                joinEntity.getQuantity(),
//                joinEntity.getId()
//        );
//    }
//}