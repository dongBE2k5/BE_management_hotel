package tdc.vn.managementhotel.ControllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.HotelDTO.HotelResponseDTO;
import tdc.vn.managementhotel.dto.HotelDTO.ViewedHotelRequestDTO;
import tdc.vn.managementhotel.service.ViewedHotelService;

import java.util.List;

@RestController
@RequestMapping("/api/viewed-hotels")
@RequiredArgsConstructor
@CrossOrigin
public class ViewedHotelController {

    private final ViewedHotelService viewedHotelService;

    @PostMapping
    public ResponseEntity<Void> saveViewedHotel(@RequestBody ViewedHotelRequestDTO request) {
        viewedHotelService.saveViewedHotel(request.getUserId(), request.getHotelId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<HotelResponseDTO>> getRecentlyViewed(@PathVariable Long userId) {
        return ResponseEntity.ok(viewedHotelService.getRecentlyViewedHotels(userId));
    }

    @GetMapping("/recently-viewed")
    public ResponseEntity<List<HotelResponseDTO>> getRecentlyViewedByUserAndLocation(
            @RequestParam Long userId,
            @RequestParam(required = false) Long locationId
    ) {
        return ResponseEntity.ok(viewedHotelService.getRecentlyViewedHotelsByLocation(userId, locationId));
    }
}
