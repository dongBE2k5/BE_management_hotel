package tdc.vn.managementhotel.ControllerAPI;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.HotelDTO.HotelDTO;
import tdc.vn.managementhotel.dto.HotelDTO.HotelResponseDTO;
import tdc.vn.managementhotel.dto.RoomDTO.RoomResponseDTO;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.service.HotelService;
import tdc.vn.managementhotel.service.RoomService;

import java.math.BigDecimal;
import java.util.List;
@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
@CrossOrigin
public class HotelController {
    private final HotelService hotelService;
    private final RoomService roomService;


    @PostMapping
    public ResponseEntity<HotelResponseDTO> createHotel(@RequestBody HotelDTO dto) {
        return ResponseEntity.ok(hotelService.createHotel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> updateHotel(@PathVariable Long id, @RequestBody HotelDTO dto) {
        return ResponseEntity.ok(hotelService.updateHotel(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> getHotel(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotel(id));
    }

    @GetMapping
    public ResponseEntity<List<HotelResponseDTO>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<HotelResponseDTO>> getAllHotelsByLocation(@PathVariable Long locationId) {
        return ResponseEntity.ok(hotelService.getHotelsByLocation(locationId));
    }

    @GetMapping("/{hotelId}/rooms")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getRoomsByHotel(hotelId));
    }

    //tim kiem
    @GetMapping("/search")
    public ResponseEntity<List<HotelResponseDTO>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        List<Hotel> hotels = hotelService.searchHotels(name, city, status, minPrice, maxPrice);

        List<HotelResponseDTO> response = hotels.stream()
                .map(hotel -> new HotelResponseDTO(
                        hotel.getId(),
                        hotel.getName(),
                        hotel.getAddress(),
                        hotel.getPhone(),
                        hotel.getImage(),
                        hotel.getEmail(),
                        hotel.getStatus(),
                        hotel.getLocation() != null ? hotel.getLocation().getName() : null,
                        hotel.getMinPrice(),
                        hotel.getMaxPrice()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }


}