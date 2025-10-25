package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tdc.vn.managementhotel.dto.RoomItemDTO.RoomItemRequestDTO;
import tdc.vn.managementhotel.dto.RoomItemDTO.RoomItemResponseDTO;
import tdc.vn.managementhotel.entity.Item;
import tdc.vn.managementhotel.entity.Room;
import tdc.vn.managementhotel.entity.TypeOfRoom;
import tdc.vn.managementhotel.entity.TypeOfRoomItem;
import tdc.vn.managementhotel.repository.ItemRepository;
import tdc.vn.managementhotel.repository.RoomRepository;
import tdc.vn.managementhotel.repository.TypeOfRoomItemRepository;
import tdc.vn.managementhotel.repository.TypeOfRoomRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TypeOfRoomItemService {

    private final TypeOfRoomRepository typeOfRoomRepository;
    private final ItemRepository itemRepository;
    private final TypeOfRoomItemRepository typeOfRoomItemRepository;
    private  final RoomRepository roomRepository;


    public List<RoomItemResponseDTO> getItemByRoomId(Long roomId){
        Room room = roomRepository.findById(roomId).orElseThrow(EntityNotFoundException::new) ;
        TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(room.getId()).orElseThrow(EntityNotFoundException::new) ;
        return typeOfRoomItemRepository.findByTypeOfRoom(typeOfRoom)
                .stream()
                .map(this::mapJoinEntityToResponseDTO)
                .collect(Collectors.toList());
    }
    /**
     * Lấy danh sách tiện ích theo loại phòng
     */
    public List<RoomItemResponseDTO> getItemsByTypeOfRoomId(Long typeOfRoomId) {
        TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(typeOfRoomId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy loại phòng có ID: " + typeOfRoomId));

        return typeOfRoomItemRepository.findByTypeOfRoom(typeOfRoom)
                .stream()
                .map(this::mapJoinEntityToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Tạo mới hoặc cập nhật danh sách tiện ích cho loại phòng
     */
    @Transactional
    public String createOrUpdateRoomItems(RoomItemRequestDTO dto) {
        // 1️⃣ Kiểm tra loại phòng
        TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(dto.getTypeOfRoomId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Không tìm thấy loại phòng có ID: " + dto.getTypeOfRoomId()));

        // 2️⃣ Duyệt danh sách item
        List<TypeOfRoomItem> toSave = dto.getItems().stream().map(itemDetail -> {
            Item item;

            // 🔍 Nếu có itemId => tìm theo ID
            if (itemDetail.getItemId() != null) {
                item = itemRepository.findById(itemDetail.getItemId()).orElse(null);
            } else {
                item = null;
            }

            // 🆕 Nếu chưa có thì tạo mới
            if (item == null) {
                item = new Item();
                item.setName(itemDetail.getItemName() != null ? itemDetail.getItemName() : "Tiện ích mới");
                item = itemRepository.save(item); // Lưu và lấy lại ID
            }

            // ⚙️ Kiểm tra xem (typeOfRoom, item) đã tồn tại chưa
            TypeOfRoomItem existing = typeOfRoomItemRepository.findByTypeOfRoomAndItem(typeOfRoom, item)
                    .orElse(null);

            if (existing != null) {
                existing.setQuantity(itemDetail.getQuantity());
                existing.setPrice(itemDetail.getPrice());
                return existing; // cập nhật
            }

            // ➕ Nếu chưa có thì tạo mới
            TypeOfRoomItem newEntity = new TypeOfRoomItem();
            newEntity.setTypeOfRoom(typeOfRoom);
            newEntity.setItem(item);
            newEntity.setQuantity(itemDetail.getQuantity());
            newEntity.setPrice(itemDetail.getPrice());
            return newEntity;
        }).collect(Collectors.toList());

        // 3️⃣ Lưu tất cả
        typeOfRoomItemRepository.saveAll(toSave);

        return "Đã thêm/cập nhật " + toSave.size() +
                " tiện ích cho loại phòng ID " + typeOfRoom.getId();
    }

    @Transactional
    public String updateRoomItemsByTypeOfRoomId(Long typeOfRoomId, RoomItemRequestDTO dto) {
        // 1️⃣ Kiểm tra loại phòng tồn tại
        TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(typeOfRoomId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Không tìm thấy loại phòng có ID: " + typeOfRoomId));

        // 2️⃣ Lấy danh sách TypeOfRoomItem hiện tại
        List<TypeOfRoomItem> existingItems = typeOfRoomItemRepository.findByTypeOfRoom(typeOfRoom);

        // 3️⃣ Duyệt danh sách từ request để cập nhật
        for (var itemDetail : dto.getItems()) {
            if (itemDetail.getItemId() == null) continue; // chỉ cập nhật item có ID

            // tìm item trong danh sách hiện tại
            existingItems.stream()
                    .filter(e -> e.getItem().getId().equals(itemDetail.getItemId()))
                    .findFirst()
                    .ifPresent(existing -> {
                        existing.setQuantity(itemDetail.getQuantity());
                        existing.setPrice(itemDetail.getPrice());
                    });
        }

        // 4️⃣ Lưu thay đổi
        typeOfRoomItemRepository.saveAll(existingItems);

        return "Đã cập nhật " + dto.getItems().size() +
                " tiện ích cho loại phòng ID " + typeOfRoomId;
    }

    /**
     * Mapper Entity → DTO
     */
    private RoomItemResponseDTO mapJoinEntityToResponseDTO(TypeOfRoomItem entity) {
        return RoomItemResponseDTO.builder()
                .typeOfRoomId(entity.getTypeOfRoom().getId())
                .typeOfRoomName(entity.getTypeOfRoom().getRoom().name()) // chú ý: phải là getName(), không phải getRoom()
                .itemId(entity.getItem().getId())
                .itemName(entity.getItem().getName())
                .quantity(entity.getQuantity())
                .price(entity.getPrice())
                .build();
    }
}
