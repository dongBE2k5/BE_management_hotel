package tdc.vn.managementhotel.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads";

    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File upload trống");
        }

        // Tạo thư mục nếu chưa có
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Tạo tên file duy nhất
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Lưu file
        Files.copy(file.getInputStream(), filePath);

        // Trả về đường dẫn tương đối
        return "/uploads/" + fileName;
    }

    public byte[] getImage(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
        if (!Files.exists(filePath)) {
            throw new IOException("Không tìm thấy file: " + fileName);
        }
        return Files.readAllBytes(filePath);
    }
}
