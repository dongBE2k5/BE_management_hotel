package tdc.vn.managementhotel.controllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tdc.vn.managementhotel.service.FileUploadService;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadControllerAPI {
    private final FileUploadService fileUploadService;

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String path = fileUploadService.uploadImage(file);
            return ResponseEntity.ok("Upload thành công: " + path);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi upload: " + e.getMessage());
        }
    }
}
