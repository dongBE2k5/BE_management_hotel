package tdc.vn.managementhotel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.config.ResourceNotFoundException;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.BookingUtilityDTO.BookingUtilityDTO;
import tdc.vn.managementhotel.dto.HotelUtilityDTO.HotelUtilityRequestDTO;
import tdc.vn.managementhotel.entity.*;
import tdc.vn.managementhotel.repository.BookingRepository;
import tdc.vn.managementhotel.repository.BookingUtilityRepository;
import tdc.vn.managementhotel.repository.HotelUtilityRepository;
import tdc.vn.managementhotel.repository.UtilityRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingUtilityService {
    private final BookingUtilityRepository bookingUtilityRepository;
    private final BookingRepository bookingRepository;
    private final UtilityRepository utilityRepository;

    public String save(BookingUtilityDTO bookingUtilityDTO) {
        Booking booking = bookingRepository.findById(bookingUtilityDTO.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking is not found"));

        List<BookingUtility> toSave = new ArrayList<>();

        for (BookingUtilityDTO.UtilityItemBooking item : bookingUtilityDTO.getUtilityItemBooking()) {
            Utility utility = utilityRepository.findById(item.getUtilityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utility is not found"));

            BookingUtility bookingUtility = new BookingUtility();
            bookingUtility.setBooking(booking);
            bookingUtility.setUtility(utility);
            bookingUtility.setQuantity(item.getQuantity());

            toSave.add(bookingUtility);
        }

        bookingUtilityRepository.saveAll(toSave); // ✅ chỉ gọi 1 lần ngoài vòng for

        return "Thêm thành công " + toSave.size() + " tiện ích cho booking " + booking.getId();
    }
}
