package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.BookingDTO.BookingRequestDTO;
import tdc.vn.managementhotel.dto.BookingDTO.BookingResponseDTO;
import tdc.vn.managementhotel.dto.BookingDTO.ChangeBookingStatusRequestDTO;
import tdc.vn.managementhotel.dto.HotelDTO.HotelBookingCountDTO;
import tdc.vn.managementhotel.dto.HotelDTO.HotelDTO;
import tdc.vn.managementhotel.dto.HotelSummaryDTO;
import tdc.vn.managementhotel.dto.LocationDTO.LocationResponseDTO;
import tdc.vn.managementhotel.dto.RoomDTO.RoomResponseDTO;
import tdc.vn.managementhotel.dto.UserDTO.UserResponse;
import tdc.vn.managementhotel.dto.VoucherDTO.VoucherResponseDTO;
import tdc.vn.managementhotel.entity.*;
import tdc.vn.managementhotel.enums.BookingStatus;
import tdc.vn.managementhotel.enums.StatusRoom;
import tdc.vn.managementhotel.repository.*;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final HistoryChangeBookingStatusRepo historyChangeBookingStatusRepo;
    private final HotelRepository hotelRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final HotelPaymentTypeRepository hotelPaymentTypeRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Transactional
    public BookingResponseDTO create(BookingRequestDTO bookingDTO) {
        Booking booking = new Booking();
        System.out.println(bookingDTO);
        mapDtoToEntity(bookingDTO, booking);

        // ✅ nếu có voucherId thì gán voucher
        if (bookingDTO.getVoucherIds() != null && !bookingDTO.getVoucherIds().isEmpty()) {
            // Chuyển List<Long> -> String "1,2"
            String joined = bookingDTO.getVoucherIds()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            booking.setVoucherIds(joined);
        }

        Booking saved = bookingRepository.save(booking);

        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "NEW_BOOKING");
        payload.put("message", "New booking created");
        payload.put("bookingId", saved.getId());

        messagingTemplate.convertAndSend("/topic/booking", payload);
        return mapEntityToResponse(saved);
    }
    public List<BookingResponseDTO> all() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }
    public BookingResponseDTO find(Long id) {
        return bookingRepository.findById(id)
                .map(this::mapEntityToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
    }

    public List<BookingResponseDTO> findByHotelID(Long id) {
        return bookingRepository.findByRoom_Hotel_Id(id)
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    public ResponseEntity<ApiResponse> findByRoomId(Long roomId) {
        List<Booking> bookings = bookingRepository.findByRoomId(roomId);

        if (bookings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(
                            HttpStatus.NOT_FOUND.value(),
                            "Không tìm thấy booking nào cho roomId = " + roomId,
                            null,
                            LocalDateTime.now()
                    ));
        }

        List<BookingResponseDTO> responseList = bookings.stream()
                .map(this::mapEntityToResponse)
                .toList();

        return ResponseEntity.ok(
                new ApiResponse(
                        HttpStatus.OK.value(),
                        "Lấy danh sách booking theo roomId thành công",
                        responseList,
                        LocalDateTime.now()
                )
        );
    }

    public List<BookingResponseDTO> findByUserID(Long id) {
        return bookingRepository.findByUser_Id(id)
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    public BookingResponseDTO updateStatus(ChangeBookingStatusRequestDTO changeBookingStatusRequestDTO) {
        Booking booking = bookingRepository.findById(changeBookingStatusRequestDTO.getBookingId()).orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        booking.setStatus(changeBookingStatusRequestDTO.getNewStatus());
        if(changeBookingStatusRequestDTO.getNewStatus().toString().equals("CHECK_OUT")){
            Map<String, Object> payload = new HashMap<>();
            payload.put("type", "CHECKOUT_BOOKING");
            payload.put("message", "Đã check out thành công ");
        messagingTemplate.convertAndSend("/topic/booking", payload);

        }

        return mapEntityToResponse(bookingRepository.save(booking));
    }


    private void mapDtoToEntity(BookingRequestDTO dto, Booking booking) {
        booking.setCheckInDate(dto.getCheckInDate());
        booking.setCheckOutDate(dto.getCheckOutDate());
        HotelPaymentType hotelPaymentType = hotelPaymentTypeRepository.findById(dto.getHotelPaymentTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        booking.setHotelPaymentType(hotelPaymentType);
        if (hotelPaymentType.getPaymentType().getId() == 1) {
            booking.setStatus(BookingStatus.DA_THANH_TOAN);
            booking.setPaidPrice(dto.getPaidPrice());
        }else if (hotelPaymentType.getPaymentType().getId() == 2) {
            booking.setStatus(BookingStatus.DA_COC);
            booking.setPaidPrice(dto.getPaidPrice());
        }else {
            booking.setStatus(BookingStatus.CHUA_THANH_TOAN);
        }
        booking.setTotalPrice(dto.getTotalPrice());
        booking.setPaymentMethod(dto.getPaymentMethod());
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        booking.setUser(user);

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        booking.setRoom(room);

    }
    private UserResponse mapEntityToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getCccd(),
                user.getRole(),
                user.getGender(),
                user.getBirthDate(),
                user.getAddress()
        );
    }

    private RoomResponseDTO mapEntityToResponse(Room room) {
        return new RoomResponseDTO(
                room.getId(),
                room.getRoomNumber(),
                room.getDescription(),
                room.getStatus(),
                room.getTypeOfRoom().getRoom(),
                room.getPrice(),
                new HotelSummaryDTO(room.getHotel().getId(), room.getHotel().getName()),
                room.getTypeOfRoom().getId()
        );
    }


    private BookingResponseDTO mapEntityToResponse(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                mapEntityToResponse(booking.getUser()),
                mapEntityToResponse(booking.getRoom()),
                booking.getStatus(),
                booking.getTotalPrice(),
                getImageHotel(booking.getRoom().getHotel().getId()),
                booking.getCreatedAt(),
                booking.getUpdatedAt(),
                booking.getVoucherIds(),
                booking.getPaidPrice(),
                booking.getHotelPaymentType() != null ? booking.getHotelPaymentType().getPaymentType().getPaymentType().name() : null,
                booking.getPaymentMethod()
        );
    }

    private String getImageHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        return hotel.getImage();
    }

    //loc bookinh lay bestchoice, co loc theo location
    public List<HotelBookingCountDTO> getBestChoiceHotels() {
        Pageable topFive = PageRequest.of(0, 5);
        List<Object[]> results = bookingRepository.findTop5HotelsWithMostBookings(topFive);

        return results.stream().map(row -> {
            Hotel hotel = (Hotel) row[0];
            Long totalBookings = (Long) row[1];

            HotelBookingCountDTO dto = new HotelBookingCountDTO();
            dto.setId(hotel.getId());
            dto.setName(hotel.getName());
            dto.setImage(hotel.getImage());
            dto.setLocation(new LocationResponseDTO(hotel.getLocation().getId(), hotel.getLocation().getName()));
            dto.setStatus(hotel.getStatus());
            dto.setTotalBookings(totalBookings);
            return dto;
        }).toList();
    }

    public List<HotelBookingCountDTO> getBestChoiceHotelsByLocation(Long locationId) {
        Pageable topFive = PageRequest.of(0, 5);
        List<Object[]> results = bookingRepository.findTop5HotelsWithMostBookingsByLocation(locationId, topFive);
        return results.stream().map(row -> {
            Hotel hotel = (Hotel) row[0];
            Long totalBookings = (Long) row[1];

            HotelBookingCountDTO dto = new HotelBookingCountDTO();
            dto.setId(hotel.getId());
            dto.setName(hotel.getName());
            dto.setImage(hotel.getImage());
            dto.setLocation(new LocationResponseDTO(hotel.getLocation().getId(), hotel.getLocation().getName()));
            dto.setStatus(hotel.getStatus());
            dto.setTotalBookings(totalBookings);
            return dto;
        }).toList();
    }
}
