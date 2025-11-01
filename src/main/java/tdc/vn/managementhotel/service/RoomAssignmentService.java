package tdc.vn.managementhotel.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tdc.vn.managementhotel.dto.RoomAssignmentDTO.RoomAssignmentRequestDTO;
import tdc.vn.managementhotel.dto.RoomAssignmentDTO.RoomAssignmentResponseDTO;
import tdc.vn.managementhotel.entity.*;
import tdc.vn.managementhotel.enums.AssignmentStatus;
import tdc.vn.managementhotel.enums.StatusRoom;
import tdc.vn.managementhotel.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomAssignmentService {

    private final RoomAssignmentRepository roomAssignmentRepository;

    private final RoomRepository roomRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RequestStaffRepository requestRepository;

    public RoomAssignmentResponseDTO mapEntityToResponse(RoomAssignment entity) {
        RoomAssignmentResponseDTO dto = new RoomAssignmentResponseDTO();
        dto.setId(entity.getId());
        dto.setRoomId(entity.getRoom().getId());
        dto.setRoomNumber(entity.getRoom().getRoomNumber());
        dto.setRoomType(entity.getRoom().getTypeOfRoom().getRoom().name());
        dto.setEmployeeId(entity.getEmployee().getId());
        dto.setEmployeeName(entity.getEmployee().getUser().getFullName());
        dto.setAssignedById(entity.getCreatedBy().getId());
        dto.setAssignedByName(entity.getCreatedBy().getUser().getFullName());
        dto.setStatus(entity.getStatus());
        dto.setNote(entity.getNote());
        dto.setAssignedAt(entity.getAssignedAt());
        dto.setCompletedAt(entity.getCompletedAt());
        dto.setRequestId(entity.getRequestStaff().getId());
        return dto;
    }

    // --- Tạo mới một RoomAssignment ---
    @Transactional
    public RoomAssignmentResponseDTO assignRoom(RoomAssignmentRequestDTO requestDTO) {

        Room room = roomRepository.findById(requestDTO.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        Employee employee = employeeRepository.findByUserId(requestDTO.getAssignedById())
                .orElseThrow(() -> new RuntimeException("Cleaning not found"));

        Employee employeeCreatedBy = employeeRepository.findByUserId(requestDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        RequestStaff requestStaff = requestRepository.findById(requestDTO.getRequestId())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        room.setStatus(StatusRoom.REQUEST);
        roomRepository.save(room);
        // Tạo entity mới
        RoomAssignment assignment = new RoomAssignment();
        assignment.setRoom(room);
        assignment.setEmployee(employee);
        assignment.setCreatedBy(employeeCreatedBy);
        assignment.setNote(requestDTO.getNote());
        assignment.setStatus(AssignmentStatus.PENDING);
        assignment.setAssignedAt(LocalDateTime.now());
        assignment.setRequestStaff(requestStaff);
        RoomAssignment saved = roomAssignmentRepository.save(assignment);
    ;
        return mapEntityToResponse(saved);
    }

    // --- Cập nhật trạng thái ---
    @Transactional
    public RoomAssignmentResponseDTO updateStatus(Long id, AssignmentStatus newStatus) {
        RoomAssignment assignment = roomAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignment.setStatus(newStatus);
        if (newStatus == AssignmentStatus.COMPLETED) {
            assignment.setCompletedAt(LocalDateTime.now());
        }
        if (newStatus == AssignmentStatus.IN_PROGRESS) {
            assignment.setAcceptedAt(LocalDateTime.now());
        }
        RoomAssignment updated = roomAssignmentRepository.save(assignment);
        return mapEntityToResponse(updated);
    }

    // --- Lấy danh sách tất cả RoomAssignment ---
    public List<RoomAssignmentResponseDTO> getAllAssignments() {
        return roomAssignmentRepository.findAll()
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    // --- Lấy danh sách phân công theo nhân viên ---
    public List<RoomAssignmentResponseDTO> getAssignmentsByEmployee(Long employeeId) {
        Employee employee = employeeRepository.findEmployeeByUserId(employeeId);
        return roomAssignmentRepository.findByEmployeeId(employee.getId())
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }


}
