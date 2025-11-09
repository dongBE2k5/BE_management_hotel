package tdc.vn.managementhotel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.config.ResourceNotFoundException;
import tdc.vn.managementhotel.dto.BookingUtilityDTO.BookingUtilityDTO;
import tdc.vn.managementhotel.dto.BookingUtilityDTO.BookingUtilityResponseDTO;
import tdc.vn.managementhotel.entity.*;
import tdc.vn.managementhotel.repository.BookingRepository;
import tdc.vn.managementhotel.repository.BookingUtilityRepository;
import tdc.vn.managementhotel.repository.UtilityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public BookingUtilityResponseDTO getBooking(Long bookingId) {
        List<BookingUtility> bookingUtilities = bookingUtilityRepository.findByBookingId(bookingId);

        if (bookingUtilities.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy tiện ích cho booking ID: " + bookingId);
        }

        return mapToResponse(bookingUtilities);
    }

    private BookingUtilityResponseDTO mapToResponse(List<BookingUtility> bookingUtilities) {
        Booking booking = bookingUtilities.get(0).getBooking();

        List<BookingUtilityResponseDTO.UtilityItemBookingResponse> items = bookingUtilities.stream()
                .map(bu -> BookingUtilityResponseDTO.UtilityItemBookingResponse.builder()
                        .utilityName(bu.getUtility().getName())
                        .quantity(bu.getQuantity())
                        .price(bu.getUtility().getPrice())
                        .build())
                .collect(Collectors.toList());

        return BookingUtilityResponseDTO.builder()
                .bookingId(booking.getId())
                .utilityItemBookingResponse(items)
                .build();
    }

}
