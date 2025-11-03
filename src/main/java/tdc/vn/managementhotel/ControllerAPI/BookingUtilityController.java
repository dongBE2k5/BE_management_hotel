package tdc.vn.managementhotel.ControllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.BookingUtilityDTO.BookingUtilityDTO;
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
}
