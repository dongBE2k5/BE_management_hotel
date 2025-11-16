package tdc.vn.managementhotel.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.RoomItemDTO.RoomItemRequestDTO;
import tdc.vn.managementhotel.dto.RoomItemDTO.RoomItemResponseDTO;
import tdc.vn.managementhotel.service.TypeOfRoomItemService;

import java.util.List;

@RestController
@RequestMapping("/api/type-of-room-items")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class TypeOfRoomItemControllerAPI {

    private final TypeOfRoomItemService typeOfRoomItemService;

    /**
     * 📥 Thêm hoặc cập nhật danh sách tiện ích cho loại phòng
     */
    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponse<String>> createOrUpdateRoomItems(@RequestBody RoomItemRequestDTO request) {

            String result = typeOfRoomItemService.createOrUpdateRoomItems(request);

            return ResponseEntity.ok(ApiResponse.success("Thành công", result));

    }

    /**
     * 🔍 Lấy danh sách tiện ích theo loại phòng
     */
    @GetMapping("/type/{typeOfRoomId}")
    public ResponseEntity<ApiResponse<List<RoomItemResponseDTO>>> getItemsByTypeOfRoomId(@PathVariable Long typeOfRoomId) {

            List<RoomItemResponseDTO> result = typeOfRoomItemService.getItemsByTypeOfRoomId(typeOfRoomId);
            return ResponseEntity.ok(ApiResponse.success("Lấy danh sách tiện ích theo room thành công", result));
    }


    @GetMapping("/type/{typeOfRoomId}/{hotelId}")
    public ResponseEntity<ApiResponse<List<RoomItemResponseDTO>>> getItemsByTypeOfRoomId(@PathVariable Long typeOfRoomId,@PathVariable Long hotelId) {

        List<RoomItemResponseDTO> result = typeOfRoomItemService.getItemsByHotel(typeOfRoomId,hotelId);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách tiện ích theo room thành công", result));
    }


    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse<List<RoomItemResponseDTO>>> getItemsByTypeOfRoomOfHotel(@PathVariable Long hotelId) {

        List<RoomItemResponseDTO> result = typeOfRoomItemService.getItemsByHotelId(hotelId);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách tiện ích theo room thành công", result));
    }

    /**
     * 🔍 Lấy danh sách tiện ích theo roomId (phòng cụ thể)
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<List<RoomItemResponseDTO>>> getItemsByRoomId(@PathVariable Long roomId) {

            List<RoomItemResponseDTO> result = typeOfRoomItemService.getItemByRoomId(roomId);

            return ResponseEntity.ok(ApiResponse.success("Lấy danh sách tiện ích theo room thành công", result));
    }

    /**
     * 🛠️ Cập nhật danh sách tiện ích theo ID loại phòng (ghi đè toàn bộ danh sách cũ)
     */
    @PutMapping("/type/{typeOfRoomId}")
    public ResponseEntity<ApiResponse<String>> updateRoomItemsByTypeId(
            @PathVariable Long typeOfRoomId,
            @RequestBody RoomItemRequestDTO request
    ) {

            String result = typeOfRoomItemService.updateRoomItemsByTypeOfRoomId(typeOfRoomId, request);
        return ResponseEntity.ok(ApiResponse.success("Update thành công", result));


    }
}
