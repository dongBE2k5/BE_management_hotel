package tdc.vn.managementhotel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tdc.vn.managementhotel.dto.RequestStaffDTO.RequestStaffRequestDTO;
import tdc.vn.managementhotel.dto.RequestStaffDTO.RequestStaffResponseDTO;
import tdc.vn.managementhotel.dto.RoomAssignmentDTO.RoomAssignmentRequestDTO;
import tdc.vn.managementhotel.entity.RequestStaff;
import tdc.vn.managementhotel.enums.RequestStatus;
import tdc.vn.managementhotel.repository.RequestStaffRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestStaffService {

    private final RequestStaffRepository repo;
    private final RoomAssignmentService roomAssignmentService;

    // ---------------------- Mapper ---------------------- //

    private RequestStaffResponseDTO mapToResponse(RequestStaff entity) {
        return new RequestStaffResponseDTO(
                entity.getId(),
                entity.getSenderId(),
                entity.getReceiverId(),
                entity.getContent(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()

        );
    }

    private RequestStaff mapToEntity(RequestStaffRequestDTO dto) {
        RequestStaff entity = new RequestStaff();
        entity.setSenderId(dto.getSenderId());
        entity.setReceiverId(dto.getReceiverId());
        entity.setContent(dto.getContent());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : RequestStatus.SENT);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    // ---------------------- CRUD logic ---------------------- //

    @Transactional
    public RequestStaffResponseDTO create(RequestStaffRequestDTO dto) {
        RequestStaff saved = repo.save(mapToEntity(dto));
        return mapToResponse(saved);
    }

    /**
     * Tạo request đồng thời gán nhiệm vụ dọn phòng.
     */
    @Transactional
    public RequestStaffResponseDTO createByRoomAssignment(RequestStaffRequestDTO dto, Long roomId,Long bookingId) {
        // 1. Tạo yêu cầu nhân viên
        RequestStaff saved = repo.save(mapToEntity(dto));

        // 2. Tạo nhiệm vụ dọn phòng tương ứng
        RoomAssignmentRequestDTO requestDTO = new RoomAssignmentRequestDTO(
                roomId,
                dto.getSenderId(),   // người gửi yêu cầu (lễ tân)
                dto.getReceiverId(), // nhân viên dọn phòng
                saved.getId(),
                dto.getContent(),
                bookingId
        );

        // Quan trọng: assignRoom phải trả về DTO, KHÔNG trả entity RoomAssignment
        roomAssignmentService.assignRoom(requestDTO);

        return mapToResponse(saved);
    }

    // ---------------------- Truy vấn ---------------------- //

    public List<RequestStaffResponseDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RequestStaffResponseDTO getById(Long id) {
        RequestStaff entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("RequestStaff not found with id " + id));
        return mapToResponse(entity);
    }

    // ---------------------- Cập nhật & xóa ---------------------- //

    @Transactional
    public RequestStaffResponseDTO update(Long id, RequestStaffRequestDTO dto) {
        RequestStaff entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("RequestStaff not found with id " + id));

        if (dto.getContent() != null) entity.setContent(dto.getContent());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(repo.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("RequestStaff not found with id " + id);
        }
        repo.deleteById(id);
    }
}
