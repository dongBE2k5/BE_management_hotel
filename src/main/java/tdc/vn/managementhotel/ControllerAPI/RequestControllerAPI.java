package tdc.vn.managementhotel.controllerAPI;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.entity.Request;
import tdc.vn.managementhotel.enums.RequestStatus;
import tdc.vn.managementhotel.repository.RequestRepository;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class RequestControllerAPI {

    private final SimpMessagingTemplate messagingTemplate;


    private final RequestRepository repo;

    public RequestControllerAPI(SimpMessagingTemplate messagingTemplate, RequestRepository repo) {
        this.messagingTemplate = messagingTemplate;
        this.repo = repo;
    }

    // User A gửi request cho B
    @PostMapping
    public ResponseEntity<Request> create(@RequestBody Request req) {
        Request saved = repo.save(req);
        // Gửi realtime tới B
        messagingTemplate.convertAndSend("/topic/user." + req.getReceiverId(), saved);
        return ResponseEntity.ok(saved);
    }

    // User B phản hồi (Accept / Reject)
    @PutMapping("/{id}/status")
    public ResponseEntity<Request> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Request req = repo.findById(id).orElseThrow();
        req.setStatus(RequestStatus.valueOf(status.toUpperCase()));
        repo.save(req);

        // Gửi realtime tới A để biết phản hồi
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
