package tdc.vn.managementhotel.ControllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.BookingUtilityDTO.BookingUtilityDTO;
import tdc.vn.managementhotel.dto.BookingUtilityDTO.BookingUtilityResponseDTO;
import tdc.vn.managementhotel.service.BookingUtilityService;

@RestController
@RequestMapping("/api/booking-utility")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class BookingUtilityController {
    private final BookingUtilityService bookingUtilityService;

    @PostMapping()
    public String insert(@RequestBody BookingUtilityDTO bookingUtilityDTO) {
        return bookingUtilityService.save(bookingUtilityDTO);
    }
    @GetMapping("/{bookingId}/booking")
    public ResponseEntity<ApiResponse< BookingUtilityResponseDTO>> getBooking(@PathVariable("bookingId") Long bookingId) {
        BookingUtilityResponseDTO result = bookingUtilityService.getBooking(bookingId);
        return ResponseEntity.ok(ApiResponse.success("Lấy thành công Booking Utility", result));

    }
}
