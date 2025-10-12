package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.BookingDTO.BookingResponseDTO;
import tdc.vn.managementhotel.dto.BookingDTO.ChangeBookingStatusRequestDTO;
import tdc.vn.managementhotel.dto.BookingDTO.ChangeBookingStatusResponseDTO;
import tdc.vn.managementhotel.dto.RoomDTO.RoomResponseDTO;
import tdc.vn.managementhotel.dto.UserDTO.UserResponse;
import tdc.vn.managementhotel.entity.*;
import tdc.vn.managementhotel.repository.BookingRepository;
import tdc.vn.managementhotel.repository.HistoryChangeBookingStatusRepo;
import tdc.vn.managementhotel.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryChangeBookingStatusService {
    private final HistoryChangeBookingStatusRepo historyChangeBookingStatusRepo;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public ChangeBookingStatusResponseDTO store(ChangeBookingStatusRequestDTO changeBookingStatusRequestDTO) {
        HistoryChangeBookingStatus historyChangeBookingStatus = new HistoryChangeBookingStatus();
        mapDtoToEntity(changeBookingStatusRequestDTO, historyChangeBookingStatus);
        return mapEntityToResponse(historyChangeBookingStatusRepo.save(historyChangeBookingStatus));
    }
    public List<ChangeBookingStatusResponseDTO> getByBookingID(Long bookingId ) {
        return historyChangeBookingStatusRepo.findByBooking_Id(bookingId)
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    private ChangeBookingStatusResponseDTO mapEntityToResponse(HistoryChangeBookingStatus changeBookingStatus) {
        return new ChangeBookingStatusResponseDTO(
                changeBookingStatus.getId(),
                mapEntityToResponse(changeBookingStatus.getBooking()),
                changeBookingStatus.getOldStatus(),
                changeBookingStatus.getNewStatus(),
                changeBookingStatus.getCreatedAt(),
                mapEntityToResponse(changeBookingStatus.getChangedBy())
        );
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
                booking.getTotalPrice()
        );
    }

    private void mapDtoToEntity(ChangeBookingStatusRequestDTO dto, HistoryChangeBookingStatus historyChangeBookingStatus) {
        historyChangeBookingStatus.setNewStatus(dto.getNewStatus());

        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        historyChangeBookingStatus.setBooking(booking);
        historyChangeBookingStatus.setOldStatus(booking.getStatus());

        User user = userRepository.findById(dto.getChangedBy())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        historyChangeBookingStatus.setChangedBy(user);
    }
}
