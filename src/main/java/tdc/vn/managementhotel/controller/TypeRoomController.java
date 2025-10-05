package tdc.vn.managementhotel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.ImageRoomDTO.ImageRoomResponseDTO;
import tdc.vn.managementhotel.dto.RoomDTO.RoomRequestDTO;
import tdc.vn.managementhotel.dto.RoomDTO.RoomResponseDTO;
import tdc.vn.managementhotel.service.HotelService;
import tdc.vn.managementhotel.service.ImageRoomService;
import tdc.vn.managementhotel.service.RoomService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/type-rooms")
@RequiredArgsConstructor
public class TypeRoomController {
    private final ImageRoomService imageRoomService;

    @GetMapping("/hotel/{id}")
    public ResponseEntity<List<ImageRoomResponseDTO>> getImageByHotel(@PathVariable Long id) {
        System.out.println("hotel id : " + id);
        return ResponseEntity.ok(imageRoomService.getByHotelID(id));
    }
    @GetMapping("/a")
    public String test() {
        return "test";
    }

}
