package tdc.vn.managementhotel.ControllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.ImageRoomDTO.ImageRoomRequestDTO;
import tdc.vn.managementhotel.dto.ImageRoomDTO.ImageRoomResponseDTO;
import tdc.vn.managementhotel.dto.TypeOfRoomDTO.TypeOfRoomDTO;
import tdc.vn.managementhotel.entity.TypeOfRoom;
import tdc.vn.managementhotel.service.ImageRoomService;
import tdc.vn.managementhotel.service.TypeOfRoomService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/type-rooms")
@RequiredArgsConstructor
public class TypeRoomController {
    private final ImageRoomService imageRoomService;
    private final TypeOfRoomService typeOfRoomService;

    @GetMapping("/hotel/{id}")
    public ResponseEntity<List<ImageRoomResponseDTO>> getImageByHotel(@PathVariable Long id) {
        System.out.println("hotel id : " + id);
        return ResponseEntity.ok(imageRoomService.getByHotelID(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllTypeRooms() {
        return typeOfRoomService.all();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeOfRoomDTO> getById(@PathVariable Long id) {
        System.out.println("id : " + id);
        return ResponseEntity.ok(typeOfRoomService.findID(id));
    }

//    @GetMapping("/by-hotel/{id}")
//    public ResponseEntity<ApiResponse> getByHotelId(@PathVariable Long id) {
//        return typeOfRoomService.findByHotelId(id);
//    }
    @GetMapping("/by-hotel/{id}")
    public ResponseEntity<ApiResponse> getAllByHotelId(@PathVariable Long id) {
        return imageRoomService.getImagesGroupedByType(id);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse> createTypeRoomByHotel(@RequestBody ImageRoomRequestDTO dto) {
        return imageRoomService.create(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTypeRoomByHotel(@PathVariable Long id, @RequestBody ImageRoomRequestDTO dto) {
        return imageRoomService.update(id, dto);
    }

    @DeleteMapping("/{typeRoomId}/hotel/{id}")
    public ResponseEntity<ApiResponse> deleteTypeRoomByHotel(@PathVariable Long typeRoomId, @PathVariable Long id) {
        return imageRoomService.delete(id, typeRoomId);
    }


}
