package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tdc.vn.managementhotel.dto.DamageItemDTO.DamagedItemRequestDTO;
import tdc.vn.managementhotel.dto.DamageItemDTO.DamagedItemResponseDTO;
import tdc.vn.managementhotel.entity.*;
import tdc.vn.managementhotel.enums.DamageStatus;
import tdc.vn.managementhotel.repository.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class DamagedItemService {

    private final DamagedItemRepository damagedItemRepository;
    private final RoomRepository roomRepository;
    private final ItemRepository itemRepository;
    private final TypeOfRoomItemRepository typeOfRoomItemRepository;
    private final UserRepository userRepository;
    private final RequestStaffRepository requestStaffRepository;
    private final BookingRepository bookingRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public DamagedItemResponseDTO reportDamage(DamagedItemRequestDTO dto) {
        System.out.println("dto"+ dto.toString());
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phòng ID: " + dto.getRoomId()));

        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy vật phẩm ID: " + dto.getItemId()));

//        User user = userRepository.findById(dto.getReportedBy())
//                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy user: " + dto.getReportedBy()));
        Employee employee = employeeRepository.findEmployeeByUserId(dto.getReportedBy())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy user: " + dto.getReportedBy()));

        RequestStaff requestStaff = requestStaffRepository.findById(dto.getRequestStaffId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy requestStaff: " + dto.getRequestStaffId()));

        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy requestStaff: " + dto.getBookingId()));
        DamagedItem entity = new DamagedItem();
        entity.setRoom(room);
        entity.setItem(item);
        entity.setQuantityAffected(dto.getQuantityAffected());
        entity.setStatus(dto.getStatus());
        entity.setImage(dto.getImage());
        entity.setEmployee(employee);
        entity.setRequestStaff(requestStaff);
        entity.setBooking(booking);
        DamagedItem saved = damagedItemRepository.save(entity);
        return mapToResponse(saved);
    }


    @Transactional
    public List<DamagedItemResponseDTO> reportMultipleDamages(List<DamagedItemRequestDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentException("Danh sách báo cáo không được để trống");
        }

        Long roomId = dtoList.get(0).getRoomId();
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phòng ID: " + roomId));

//        User user = userRepository.findById(dtoList.get(0).getReportedBy())
//                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy user: " + dtoList.get(0).getReportedBy()));
        Employee employee = employeeRepository.findEmployeeByUserId(dtoList.get(0).getReportedBy())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy user: " + dtoList.get(0).getReportedBy()));
        RequestStaff requestStaff = requestStaffRepository.findById(dtoList.get(0).getRequestStaffId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy requestStaff: " + dtoList.get(0).getRequestStaffId()));

        Booking booking = bookingRepository.findById(dtoList.get(0).getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy booking: " + dtoList.get(0).getBookingId()));
        // Đảm bảo tất cả cùng 1 phòng
        boolean allSameRoom = dtoList.stream().allMatch(d -> d.getRoomId().equals(roomId));
        if (!allSameRoom) {
            throw new IllegalArgumentException("Tất cả vật phẩm phải thuộc cùng một phòng");
        }

        List<DamagedItem> savedList = dtoList.stream().map(dto -> {
            Item item = itemRepository.findById(dto.getItemId())
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy vật phẩm ID: " + dto.getItemId()));

            DamagedItem entity = new DamagedItem();
            entity.setRoom(room);
            entity.setItem(item);
            entity.setQuantityAffected(dto.getQuantityAffected());
            entity.setStatus(dto.getStatus());
            entity.setImage(dto.getImage());
            entity.setEmployee(employee);
            entity.setRequestStaff(requestStaff);
            entity.setBooking(booking);
            return damagedItemRepository.save(entity);
        }).collect(Collectors.toList());

        return savedList.stream().map(this::mapToResponse).collect(Collectors.toList());
    }


    public List<DamagedItemResponseDTO> getAll() {
        return damagedItemRepository.findAll()
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DamagedItemResponseDTO> getByStatus(String status) {
        return damagedItemRepository.findByStatus(Enum.valueOf(DamageStatus.class, status.toUpperCase()))
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DamagedItemResponseDTO> getByRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phòng ID: " + roomId));

        return damagedItemRepository.findByRoom(room)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DamagedItemResponseDTO> getByRequest(Long requestStaffId) {
        RequestStaff requestStaff = requestStaffRepository.findById(requestStaffId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy request: " + requestStaffId));
        return damagedItemRepository.findByRequestStaff(requestStaff)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DamagedItemResponseDTO> getByBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy booking: " + bookingId));
        return damagedItemRepository.findByBooking(booking)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private DamagedItemResponseDTO mapToResponse(DamagedItem entity) {
        TypeOfRoomItem getprice =typeOfRoomItemRepository.findByItemId(entity.getItem().getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy request: " + entity.getItem().getId()));


        return DamagedItemResponseDTO.builder()
                .requestStaffId(entity.getRequestStaff().getId())
                .id(entity.getId())
                .roomId(entity.getRoom().getId())
                .roomNumber(entity.getRoom().getRoomNumber())
                .itemId(entity.getItem().getId())
                .itemName(entity.getItem().getName())
                .quantityAffected(entity.getQuantityAffected())
                .status(entity.getStatus())
                .image(entity.getImage())
                .reportedBy(entity.getEmployee().getUser().getFullName())
                .reportedAt(entity.getReportedAt())
                .price(getprice.getPrice())
                .bookingId(entity.getBooking().getId())
                .build();
    }
}
