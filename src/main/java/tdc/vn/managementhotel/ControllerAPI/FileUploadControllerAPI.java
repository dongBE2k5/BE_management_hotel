package tdc.vn.managementhotel.ControllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tdc.vn.managementhotel.service.FileUploadService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class FileUploadControllerAPI {
    private final FileUploadService fileUploadService;

    // Upload 1 file (vẫn giữ lại nếu cần)
    @PostMapping("/single")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String path = fileUploadService.uploadImage(file);
                // Lấy đường dẫn của file đã upload có dạng localhost:8080/uploads/...
                String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
                String fullPath = baseUrl + "/uploads/" + path;
            return ResponseEntity.ok("Upload thành công: " + fullPath);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi upload: " + e.getMessage());
        }
    }

    // Upload nhiều file
    @PostMapping("/multiple")
    public ResponseEntity<?> uploadMultipleImages(@RequestParam("files") MultipartFile[] files)  {
        List<String> uploadedPaths = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String path = fileUploadService.uploadImage(file);
                uploadedPaths.add(path);
            }
            return ResponseEntity.ok("Upload thành công: " +uploadedPaths);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi upload: " + e.getMessage());
        }
    }


    public ResponseEntity<String> deleteImage(String imageUrl) {
        try {
            fileUploadService.deleteImage(imageUrl);
            return ResponseEntity.ok("Xóa ảnh thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi xóa ảnh: " + e.getMessage());
        }
    }
}
