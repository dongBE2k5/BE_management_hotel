package tdc.vn.managementhotel.ControllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.HotelDTO.HotelResponseDTO;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.service.SavedHotelService;

import java.util.List;

@RestController
@RequestMapping("/api/saved-hotels")
@RequiredArgsConstructor
@CrossOrigin
public class SavedHotelController {

    private final SavedHotelService savedHotelService;

    //  Lưu khách sạn
    @PostMapping
    public ResponseEntity<String> saveHotel(
            @RequestParam Long userId,
            @RequestParam Long hotelId
    ) {
        savedHotelService.saveHotel(userId, hotelId);
        return ResponseEntity.ok("Đã lưu khách sạn");
    }

    //  Bỏ lưu khách sạn
    @DeleteMapping("/{userId}/{hotelId}")
    public ResponseEntity<String> removeHotel(
            @PathVariable Long userId,
            @PathVariable Long hotelId
    ) {
        savedHotelService.removeHotel(userId, hotelId);
        return ResponseEntity.ok("Đã bỏ lưu khách sạn");
    }

    //  Lấy danh sách khách sạn đã lưu
    @GetMapping("/{userId}")
    public ResponseEntity<List<HotelResponseDTO>> getSavedHotels(@PathVariable Long userId) {
        return ResponseEntity.ok(savedHotelService.getSavedHotels(userId));
    }
    // Kiểm tra 1 khách sạn đã được lưu hay chưa
    @GetMapping("/check")
    public ResponseEntity<Boolean> isHotelSaved(
            @RequestParam Long userId,
            @RequestParam Long hotelId
    ) {
        boolean isSaved = savedHotelService.isHotelSaved(userId, hotelId);
        return ResponseEntity.ok(isSaved);
    }

    //lay theo location
    @GetMapping("/{userId}/by-location/{locationId}")
    public ResponseEntity<List<HotelResponseDTO>> getSavedHotelsByLocation(
            @PathVariable Long userId,
            @PathVariable Long locationId
    ) {
        // Gọi service trả về DTO
        List<HotelResponseDTO> hotels = savedHotelService.getSavedHotelsByLocation(userId, locationId);
        return ResponseEntity.ok(hotels);
    }
}
