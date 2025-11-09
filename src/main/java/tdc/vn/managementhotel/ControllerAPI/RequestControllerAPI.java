package tdc.vn.managementhotel.controllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.RequestStaffDTO.RequestStaffRequestDTO;
import tdc.vn.managementhotel.dto.RequestStaffDTO.RequestStaffResponseDTO;
import tdc.vn.managementhotel.dto.RoomAssignmentDTO.RoomAssignmentRequestDTO;
import tdc.vn.managementhotel.entity.RequestStaff;
import tdc.vn.managementhotel.enums.AssignmentStatus;
import tdc.vn.managementhotel.enums.RequestStatus;
import tdc.vn.managementhotel.repository.RequestStaffRepository;
import tdc.vn.managementhotel.service.DamagedItemService;
import tdc.vn.managementhotel.service.RequestStaffService;
import tdc.vn.managementhotel.service.RoomAssignmentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestControllerAPI {

    private final SimpMessagingTemplate messagingTemplate;
    private final RequestStaffRepository repo;

    private final DamagedItemService  damagedItemService;
    private final RoomAssignmentService roomAssignmentService;
    private final RequestStaffService requestStaffService;



    // User A gửi request cho B
    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponse<RequestStaffResponseDTO>> create(@RequestBody RequestStaffRequestDTO req , @RequestParam Long roomId,@RequestParam Long bookingId) {
        req.setStatus(RequestStatus.SENT);
        RequestStaffResponseDTO saved = requestStaffService.createByRoomAssignment(req,roomId,bookingId);

        // Gửi realtime tới B
//        System.out.println("Request create"+saved.getId().toString());
        messagingTemplate.convertAndSend("/topic/user." + req.getReceiverId(), saved);
        return ResponseEntity.ok(ApiResponse.success("Gửi yêu cầu thành công",saved));
    }

    // User B phản hồi (Accept / Reject)
    /**
     * Nhân viên phản hồi (RECEIVED / HAS_ISSUE / NO_ISSUE)
     */
    @PutMapping("/{id}/status")
    @Transactional
    public ResponseEntity<RequestStaff> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) Long assignmentId, // để xác định nhiệm vụ liên quan
            @RequestParam Long roomId
    ) {
        RequestStaff req = repo.findById(id).orElseThrow();
        RequestStatus newStatus = RequestStatus.valueOf(status.toUpperCase());
        req.setStatus(newStatus);
        req.setUpdatedAt(LocalDateTime.now());
        repo.save(req);

        // 🧠 Tùy theo trạng thái mà cập nhật RoomAssignment
        switch (newStatus) {
            case RECEIVED:
                // nhân viên đã nhận nhiệm vụ — chuyển sang IN_PROGRESS
                if (assignmentId != null) {
                    roomAssignmentService.updateStatus(assignmentId, AssignmentStatus.IN_PROGRESS,roomId);
                }
                break;

            case HAS_ISSUE:
                // có vấn đề — cập nhật assignment sang COMPLETED (sẽ xử lý damaged item bằng API khác)
                if (assignmentId != null) {
                    roomAssignmentService.updateStatus(assignmentId, AssignmentStatus.COMPLETED,roomId);
                }
                break;

            case NO_ISSUE:
                // hoàn thành bình thường
                if (assignmentId != null) {
                    roomAssignmentService.updateStatus(assignmentId, AssignmentStatus.COMPLETED,roomId);
                }
                break;

            default:
                break;
        }

        // 🔔 Gửi realtime cho người gửi (lễ tân)
        messagingTemplate.convertAndSend("/topic/user." + req.getSenderId(), req);

        return ResponseEntity.ok(req);
    }

    // Lấy danh sách request cho 1 user
    @GetMapping("/received/{receiverId}")
    public ResponseEntity<ApiResponse<List<RequestStaff>>> getReceived(@PathVariable Long receiverId) {

       List<RequestStaff> result = repo.findByReceiverId(receiverId);
        return ResponseEntity.ok(ApiResponse.success("lấy dữ liệu thành recevier",result));

    }

    @GetMapping("/sent/{senderId}")
    public List<RequestStaff> getSent(@PathVariable Long senderId) {
        return repo.findBySenderId(senderId);
    }


}
