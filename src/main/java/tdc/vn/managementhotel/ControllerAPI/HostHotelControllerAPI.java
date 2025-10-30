package tdc.vn.managementhotel.controllerAPI;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.HostHotelDTO.HostHotelRequestDTO;
import tdc.vn.managementhotel.dto.HostHotelDTO.HostHotelResponseDTO;
import tdc.vn.managementhotel.enums.HostHotelStatus;
import tdc.vn.managementhotel.repository.HostHotelRepository;
import tdc.vn.managementhotel.service.FileUploadService;
import tdc.vn.managementhotel.service.HostHotelService;

import java.io.IOException;

@RestController
@RequestMapping("api/host")
@RequiredArgsConstructor
@CrossOrigin("*")
public class HostHotelControllerAPI {
    private final HostHotelService hostHotelService;
    private final FileUploadService fileUploadService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<HostHotelResponseDTO>> createHostHotel(
            @RequestPart("data") HostHotelRequestDTO dto,
            @RequestPart(value = "cccdMatTruoc", required = false) MultipartFile cccdMatTruoc,
            @RequestPart(value = "cccdMatSau", required = false) MultipartFile cccdMatSau,
            @RequestPart(value = "giayPhepKinhDoanh", required = false) MultipartFile giayPhepKinhDoanh
    ) throws IOException {

        // ✅ Upload từng file nếu có
        if (cccdMatTruoc != null && !cccdMatTruoc.isEmpty()) {
            dto.setCccdMatTruoc(fileUploadService.uploadImage(cccdMatTruoc));
        }

        if (cccdMatSau != null && !cccdMatSau.isEmpty()) {
            dto.setCccdMatSau(fileUploadService.uploadImage(cccdMatSau));
        }

        if (giayPhepKinhDoanh != null && !giayPhepKinhDoanh.isEmpty()) {
            dto.setGiayPhepKinhDoanh(fileUploadService.uploadImage(giayPhepKinhDoanh));
        }

        // ✅ Lưu dữ liệu
        HostHotelResponseDTO result = hostHotelService.createOrUpdate(dto);

        // ✅ Trả về dạng ApiResponse
        return ResponseEntity.ok(ApiResponse.success("Gửi xác nhận lần đầu thành công", result));
    }

    @PutMapping("/{id}/status")
    @Transactional
    public ResponseEntity<ApiResponse<HostHotelResponseDTO>> updateHostHotelStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {

        // Chuyển status từ String sang enum (uppercase để tránh lỗi chính tả)
        HostHotelStatus newStatus = HostHotelStatus.valueOf(status.toUpperCase());

        // Gọi service cập nhật trạng thái
        HostHotelResponseDTO updated = hostHotelService.updateStatus(id, newStatus);


        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", updated));
    }


    @GetMapping("/{idUser}/user")
    public ResponseEntity<ApiResponse<HostHotelResponseDTO>> getHostHotelByUserId(@PathVariable Long idUser) {
        HostHotelResponseDTO dto = hostHotelService.getByUserId(idUser);
        return ResponseEntity.ok(ApiResponse.success("Lấy thành công hostHotel", dto));
    }


}
