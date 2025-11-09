package tdc.vn.managementhotel.controllerAPI;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.DamageItemDTO.DamagedItemRequestDTO;
import tdc.vn.managementhotel.dto.DamageItemDTO.DamagedItemResponseDTO;
import tdc.vn.managementhotel.service.DamagedItemService;
import tdc.vn.managementhotel.service.FileUploadService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/damaged-items")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class DamagedItemControllerAPI {

    private final DamagedItemService damagedItemService;
    private final FileUploadService fileUploadService;

    /**
     * 🧾 Báo hư hại một vật phẩm
     */
    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponse<DamagedItemResponseDTO>> reportDamage(
            @RequestPart(value = "data") DamagedItemRequestDTO request,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        System.out.println(image);
        if (image != null ){
            request.setImage(fileUploadService.uploadImage(image));
        }
        DamagedItemResponseDTO result = damagedItemService.reportDamage(request);
        return ResponseEntity.ok(ApiResponse.success("Báo cáo hư hại thành công", result));
    }

    /**
     * 🧾 Báo nhiều vật phẩm bị hư (cùng một phòng)
     */
    @PostMapping("/batch")
    @Transactional
    public ResponseEntity<ApiResponse<List<DamagedItemResponseDTO>>> reportMultipleDamages(
            @RequestBody List<DamagedItemRequestDTO> requests) {

        List<DamagedItemResponseDTO> result = damagedItemService.reportMultipleDamages(requests);
        return ResponseEntity.ok(ApiResponse.success("Đã báo cáo " + result.size() + " vật phẩm hư hỏng", result));
    }

    /**
     * 📋 Lấy toàn bộ danh sách hư hại
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DamagedItemResponseDTO>>> getAll() {

        List<DamagedItemResponseDTO> result = damagedItemService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Danh sách vật phẩm hư hỏng", result));
    }

    /**
     * 🔍 Lấy danh sách theo trạng thái (MISSING, BROKEN, FIXED...)
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<DamagedItemResponseDTO>>> getByStatus(@PathVariable String status) {

        List<DamagedItemResponseDTO> result = damagedItemService.getByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Danh sách vật phẩm theo trạng thái: " + status, result));
    }

    /**
     * 🏠 Lấy danh sách vật phẩm hư theo phòng
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<List<DamagedItemResponseDTO>>> getByRoom(@PathVariable Long roomId) {

        List<DamagedItemResponseDTO> result = damagedItemService.getByRoom(roomId);
        return ResponseEntity.ok(ApiResponse.success("Danh sách vật phẩm hư hỏng trong phòng " + roomId, result));
    }

    @GetMapping("/request/{requestID}")
    public ResponseEntity<ApiResponse<List<DamagedItemResponseDTO>>> getByReaquest(@PathVariable Long requestID) {

        List<DamagedItemResponseDTO> result = damagedItemService.getByRequest(requestID);
        return ResponseEntity.ok(ApiResponse.success("Danh sách vật phẩm hư hỏng trong phòng " + requestID, result));
    }
    @GetMapping("/booking/{bookingID}")
    public ResponseEntity<ApiResponse<List<DamagedItemResponseDTO>>> getByBooking(@PathVariable Long bookingID) {

        List<DamagedItemResponseDTO> result = damagedItemService.getByBooking(bookingID);
        return ResponseEntity.ok(ApiResponse.success("Danh sách vật phẩm hư hỏng trong phòng booking" + bookingID, result));
    }
}

