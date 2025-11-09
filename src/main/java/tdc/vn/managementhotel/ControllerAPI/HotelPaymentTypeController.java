package tdc.vn.managementhotel.ControllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.HotelPaymentType.HotelPaymentTypeRequestDTO;
import tdc.vn.managementhotel.entity.HotelPaymentType;
import tdc.vn.managementhotel.service.HotelPaymentTypeService;

@RestController
@RequestMapping("/api/hotel-payment-type")
@CrossOrigin
@RequiredArgsConstructor
public class HotelPaymentTypeController {
    private final HotelPaymentTypeService hotelPaymentTypeService;

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse> getHotelPaymentTypeService(@PathVariable Long hotelId) {
        return hotelPaymentTypeService.findAllByHotelId(hotelId);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getHotelPaymentTypeById(@PathVariable Long id) {
        return hotelPaymentTypeService.getById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createHotelPaymentType(@RequestBody HotelPaymentTypeRequestDTO hotelPaymentTypeRequestDTO) {
        return hotelPaymentTypeService.save(hotelPaymentTypeRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateHotelPaymentType(@PathVariable Long id, @RequestBody HotelPaymentTypeRequestDTO requestDTO) {
        return hotelPaymentTypeService.update(id, requestDTO);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteHotelPaymentType(Long id) {
        return hotelPaymentTypeService.delete(id);
    }
}
