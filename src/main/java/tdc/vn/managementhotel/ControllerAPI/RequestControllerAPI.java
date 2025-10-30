package tdc.vn.managementhotel.controllerAPI;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.RoomAssignmentDTO.RoomAssignmentRequestDTO;
import tdc.vn.managementhotel.entity.Request;
import tdc.vn.managementhotel.enums.AssignmentStatus;
import tdc.vn.managementhotel.enums.RequestStatus;
import tdc.vn.managementhotel.repository.DamagedItemRepository;
import tdc.vn.managementhotel.repository.RequestRepository;
import tdc.vn.managementhotel.service.DamagedItemService;
import tdc.vn.managementhotel.service.RoomAssignmentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestControllerAPI {

    private final SimpMessagingTemplate messagingTemplate;

    private final RequestRepository repo;
    private final DamagedItemService  damagedItemService;
    private final RoomAssignmentService roomAssignmentService;



    // User A gửi request cho B
    @PostMapping
    @Transactional
    public ResponseEntity<Request> create(@RequestBody Request req ,@RequestParam Long roomId) {
        Request saved = repo.save(req);
        RoomAssignmentRequestDTO requestDTO = new RoomAssignmentRequestDTO(roomId,req.getSenderId(),req.getReceiverId(),req.getContent());
        roomAssignmentService.assignRoom(requestDTO);
        // Gửi realtime tới B
        messagingTemplate.convertAndSend("/topic/user." + req.getReceiverId(), saved);
        return ResponseEntity.ok(saved);
    }

    // User B phản hồi (Accept / Reject)
    /**
     * Nhân viên phản hồi (RECEIVED / HAS_ISSUE / NO_ISSUE)
     */
    @PutMapping("/{id}/status")
    @Transactional
    public ResponseEntity<Request> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) Long assignmentId // để xác định nhiệm vụ liên quan
    ) {
        Request req = repo.findById(id).orElseThrow();
        RequestStatus newStatus = RequestStatus.valueOf(status.toUpperCase());
        req.setStatus(newStatus);
        req.setUpdatedAt(LocalDateTime.now());
        repo.save(req);

        // 🧠 Tùy theo trạng thái mà cập nhật RoomAssignment
        switch (newStatus) {
            case RECEIVED:
                // nhân viên đã nhận nhiệm vụ — chuyển sang IN_PROGRESS
                if (assignmentId != null) {
                    roomAssignmentService.updateStatus(assignmentId, AssignmentStatus.IN_PROGRESS);
                }
                break;

            case HAS_ISSUE:
                // có vấn đề — cập nhật assignment sang COMPLETED (sẽ xử lý damaged item bằng API khác)
                if (assignmentId != null) {
                    roomAssignmentService.updateStatus(assignmentId, AssignmentStatus.COMPLETED);
                }
                break;

            case NO_ISSUE:
                // hoàn thành bình thường
                if (assignmentId != null) {
                    roomAssignmentService.updateStatus(assignmentId, AssignmentStatus.COMPLETED);
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
    public List<Request> getReceived(@PathVariable Long receiverId) {
        return repo.findByReceiverId(receiverId);
    }

    @GetMapping("/sent/{senderId}")
    public List<Request> getSent(@PathVariable Long senderId) {
        return repo.findBySenderId(senderId);
    }


}
