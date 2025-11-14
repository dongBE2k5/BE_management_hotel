package tdc.vn.managementhotel.ControllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.HotelPaymentTypeDTO.HotelPaymentTypeRequestDTO;
import tdc.vn.managementhotel.service.HotelPaymentTypeService;

@RestController
@RequestMapping("/api/hotel-payment-type")
@CrossOrigin
@RequiredArgsConstructor
public class HotelPaymentTypeController {
    private final HotelPaymentTypeService hotelPaymentTypeService;

    @PostMapping
    public ResponseEntity<ApiResponse> addPaymentType(@RequestBody HotelPaymentTypeRequestDTO dto) {
        return hotelPaymentTypeService.save(dto);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse> getPaymentTypes(@PathVariable Long hotelId) {
        return hotelPaymentTypeService.findAllByHotelId(hotelId);
    }

    @GetMapping("/hotel/{hotelId}/type-of-room/{typeOfRoomId}")
    public ResponseEntity<ApiResponse> getPaymentTypesByHotelAndTypeOfRoom(@PathVariable Long hotelId, @PathVariable Long typeOfRoomId) {
        return hotelPaymentTypeService.findAllByHotelIdAndTypeOfRoom(hotelId, typeOfRoomId);
    }

    @PutMapping()
    public ResponseEntity<ApiResponse> update(@RequestBody HotelPaymentTypeRequestDTO dto) {
        return hotelPaymentTypeService.update(dto);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        return hotelPaymentTypeService.delete(id);
    }
//    @GetMapping("/hotel/{hotelId}")
//    public ResponseEntity<ApiResponse> getHotelPaymentTypeService(@PathVariable Long hotelId) {
//        return hotelPaymentTypeService.findAllByHotelId(hotelId);
//    }
//
//    @GetMapping("{id}")
//    public ResponseEntity<ApiResponse> getHotelPaymentTypeById(@PathVariable Long id) {
//        return hotelPaymentTypeService.getById(id);
//    }
//
//    @PostMapping
//    public ResponseEntity<ApiResponse> createHotelPaymentType(@RequestBody HotelPaymentTypeRequestDTO hotelPaymentTypeRequestDTO) {
//        return hotelPaymentTypeService.save(hotelPaymentTypeRequestDTO);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResponse> updateHotelPaymentType(@PathVariable Long id, @RequestBody HotelPaymentTypeRequestDTO requestDTO) {
//        return hotelPaymentTypeService.update(id, requestDTO);
//    }
//
//    @DeleteMapping
//    public ResponseEntity<ApiResponse> deleteHotelPaymentType(Long id) {
//        return hotelPaymentTypeService.delete(id);
//    }
}
