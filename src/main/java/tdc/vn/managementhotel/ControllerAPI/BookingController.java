package tdc.vn.managementhotel.ControllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.BookingDTO.BookingRequestDTO;
import tdc.vn.managementhotel.dto.BookingDTO.BookingResponseDTO;
import tdc.vn.managementhotel.dto.BookingDTO.ChangeBookingStatusRequestDTO;
import tdc.vn.managementhotel.dto.BookingDTO.ChangeBookingStatusResponseDTO;
import tdc.vn.managementhotel.dto.HotelDTO.HotelDTO;
import tdc.vn.managementhotel.dto.HotelDTO.HotelResponseDTO;
import tdc.vn.managementhotel.service.BookingService;
import tdc.vn.managementhotel.service.HistoryChangeBookingStatusService;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final HistoryChangeBookingStatusService historyChangeBookingStatusService;
//    @PostMapping
//    public ResponseEntity<BookingResponseDTO> createBooking(@RequestParam Long userId,
//                                                            @RequestParam LocalDate checkInDate,
//                                                            @RequestParam LocalDate checkOutDate,
//                                                            @RequestParam Long roomId) {
//        BookingRequestDTO bookingRequestDTO = new BookingRequestDTO(checkInDate, checkOutDate, roomId, userId);
//        System.out.println(bookingRequestDTO.toString());
//        return ResponseEntity.ok(bookingService.create(bookingRequestDTO));
//    }
    @PostMapping()
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO) {
        System.out.println(bookingRequestDTO.toString());
        return ResponseEntity.ok(bookingService.create(bookingRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.all());
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> findBookingsId(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.find(id));
    }

    @GetMapping("/hotel/{id}")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookingsByHotelId(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findByHotelID(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookingsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findByUserID(id));
    }

    @PutMapping("/update-status")
    public BookingResponseDTO changeStatus(@RequestBody ChangeBookingStatusRequestDTO changeBookingStatusRequestDTO) {
        historyChangeBookingStatusService.store(changeBookingStatusRequestDTO);
        return bookingService.updateStatus(changeBookingStatusRequestDTO);
    }

//    @PostMapping("/change-status")
//    public ResponseEntity<ChangeBookingStatusResponseDTO> changeStatus(@RequestBody ChangeBookingStatusRequestDTO changeBookingStatusRequestDTO) {
//        System.out.println(changeBookingStatusRequestDTO.toString());
//        return ResponseEntity.ok(historyChangeBookingStatusService.store(changeBookingStatusRequestDTO));
//    }
}
