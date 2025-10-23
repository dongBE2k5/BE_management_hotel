package tdc.vn.managementhotel.ControllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.BookingDTO.BookingRequestDTO;
import tdc.vn.managementhotel.dto.BookingDTO.BookingResponseDTO;
import tdc.vn.managementhotel.dto.BookingDTO.ChangeBookingStatusRequestDTO;
import tdc.vn.managementhotel.dto.BookingDTO.ChangeBookingStatusResponseDTO;
import tdc.vn.managementhotel.dto.HotelDTO.HotelBookingCountDTO;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.service.BookingService;
import tdc.vn.managementhotel.service.HistoryChangeBookingStatusService;

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
        List<BookingResponseDTO> bookings = bookingService.findByHotelID(id);

        if (bookings.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok().body(bookings); // 200 OK + JSON list
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

    @GetMapping("/history/{bookingid}")
    public ResponseEntity<List<ChangeBookingStatusResponseDTO>> getBookingHistory(@PathVariable Long bookingid) {
        return  ResponseEntity.ok(historyChangeBookingStatusService.getByBookingID(bookingid));
    }
//    @PostMapping("/change-status")
//    public ResponseEntity<ChangeBookingStatusResponseDTO> changeStatus(@RequestBody ChangeBookingStatusRequestDTO changeBookingStatusRequestDTO) {
//        System.out.println(changeBookingStatusRequestDTO.toString());
//        return ResponseEntity.ok(historyChangeBookingStatusService.store(changeBookingStatusRequestDTO));
//    }

    //bestchoice
    @GetMapping("/best-choice")
    public ResponseEntity<?> getBestChoiceHotels(@RequestParam(required = false) Long locationId) {
        try {
            List<HotelBookingCountDTO> hotels = (locationId != null)
                    ? bookingService.getBestChoiceHotelsByLocation(locationId)
                    : bookingService.getBestChoiceHotels();
            return ResponseEntity.ok(hotels);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy Best Choice Hotels: " + e.getMessage());
        }
    }


}
