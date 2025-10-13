package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.BookingDTO.BookingRequestDTO;
import tdc.vn.managementhotel.dto.BookingDTO.BookingResponseDTO;
import tdc.vn.managementhotel.dto.BookingDTO.ChangeBookingStatusRequestDTO;
import tdc.vn.managementhotel.dto.HotelDTO.HotelDTO;
import tdc.vn.managementhotel.dto.LocationDTO.LocationResponseDTO;
import tdc.vn.managementhotel.dto.RoomDTO.RoomResponseDTO;
import tdc.vn.managementhotel.dto.UserDTO.UserResponse;
import tdc.vn.managementhotel.entity.*;
import tdc.vn.managementhotel.enums.BookingStatus;
import tdc.vn.managementhotel.enums.StatusRoom;
import tdc.vn.managementhotel.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final HistoryChangeBookingStatusRepo historyChangeBookingStatusRepo;
    private final HotelRepository hotelRepository;

    public BookingResponseDTO create(BookingRequestDTO bookingDTO) {
        Booking booking = new Booking();
        mapDtoToEntity(bookingDTO, booking);
        return mapEntityToResponse(bookingRepository.save(booking));
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

    public List<BookingResponseDTO> findByUserID(Long id) {
        return bookingRepository.findByUser_Id(id)
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    public BookingResponseDTO updateStatus(ChangeBookingStatusRequestDTO changeBookingStatusRequestDTO) {
        Booking booking = bookingRepository.findById(changeBookingStatusRequestDTO.getBookingId()).orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        booking.setStatus(changeBookingStatusRequestDTO.getNewStatus());
        return mapEntityToResponse(bookingRepository.save(booking));
    }


    private void mapDtoToEntity(BookingRequestDTO dto, Booking booking) {
        booking.setCheckInDate(dto.getCheckInDate());
        booking.setCheckOutDate(dto.getCheckOutDate());
        booking.setStatus(BookingStatus.CHUA_THANH_TOAN);
        booking.setTotalPrice(dto.getTotalPrice());

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
                user.getRole()
        );
    }

    private RoomResponseDTO mapEntityToResponse(Room room) {
        return new RoomResponseDTO(
                room.getId(),
                room.getRoomNumber(),
                room.getDescription(),
                room.getStatus(),
                room.getTypeOfRoom().getRoom(),
                room.getHotel().getName(),
                room.getPrice()

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
                getImageHotel(booking.getRoom().getHotel().getId())
        );
    }
    private String getImageHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        return hotel.getImage();
    }
}
