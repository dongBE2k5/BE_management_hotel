package tdc.vn.managementhotel.controllerAPI;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.RoomAssignmentDTO.RoomAssignmentResponseDTO;
import tdc.vn.managementhotel.service.RoomAssignmentService;

import java.util.List;

@RestController
@RequestMapping("/api/assignment")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RoomAssignmentControllerAPI {
    private final RoomAssignmentService roomAssignmentService;


    @GetMapping("/{idUser}/user")
    public ResponseEntity<ApiResponse<List<RoomAssignmentResponseDTO>>> getByUser(@PathVariable Long idUser) {
      List<RoomAssignmentResponseDTO> result= roomAssignmentService.getAssignmentsByEmployee(idUser);
        return ResponseEntity.ok(ApiResponse.success("Lấy dữ liệu roomAsssignment thành công ", result));
    }
}
