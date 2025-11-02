package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tdc.vn.managementhotel.config.ResourceNotFoundException;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.HotelDTO.HotelResponseDTO;
import tdc.vn.managementhotel.dto.LocationDTO.LocationResponseDTO;
import tdc.vn.managementhotel.dto.UtilityDTO.UtilityDTO;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.Location;
import tdc.vn.managementhotel.entity.Utility;
import tdc.vn.managementhotel.enums.UtilityType;
import tdc.vn.managementhotel.enums.UtilityUsed;
import tdc.vn.managementhotel.repository.HotelRepository;
import tdc.vn.managementhotel.repository.LocationRepository;
import tdc.vn.managementhotel.repository.UtilityRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UtilityService {
    private final UtilityRepository utilityRepository;
    private final HotelRepository hotelRepository;
    private final FileUploadService fileUploadService;
    public ResponseEntity<ApiResponse> create(UtilityDTO utilityDTO, MultipartFile image) throws IOException {
        // Upload ảnh
        if (image != null) {
            String imagePath = fileUploadService.uploadImage(image);
            utilityDTO.setImageUrl(imagePath);
        }else {
            utilityDTO.setImageUrl(null);
        }


        Utility utility = new Utility();
        mapDTOToEntity(utilityDTO, utility);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        "Tạo tiện ích thành công",
                        mapEntityToResponse(utilityRepository.save(utility)),
                        LocalDateTime.now()));
    }
    public ResponseEntity<ApiResponse> getById(Long id) {
        Utility utility = utilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utility is not found"));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(), "Lấy tiện ích thành công", mapEntityToResponse(utility), LocalDateTime.now()));
    }
    public ResponseEntity<ApiResponse> getAll() {
        List<Utility> utilities = utilityRepository.findAll();
        List<UtilityDTO> utilityDTOs = utilities.stream().map(this::mapEntityToResponse).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(), "Lấy tất cả tiện ích thành công", utilityDTOs, LocalDateTime.now()));
    }
    public ResponseEntity<ApiResponse> getByType(UtilityType type) {
        List<Utility> utilities = utilityRepository.findByType(type);
        List<UtilityDTO> utilityDTOs = utilities.stream().map(this::mapEntityToResponse).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        "Lấy tiện ích thành công",
                        utilityDTOs,
                        LocalDateTime.now()));
    }
    public ResponseEntity<ApiResponse> update(Long id, UtilityDTO utilityDTO, MultipartFile image) throws IOException {
        // 1️⃣ Lấy tiện ích hiện tại để biết ảnh cũ
        Utility existingUtility = utilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utility not found with id: " + id));
        // 2️⃣ Nếu có ảnh mới được gửi lên
        if (image != null && !image.isEmpty()) {
            // Xóa ảnh cũ (nếu có)
            if (existingUtility.getImageUrl() != null) {
                System.out.println("Xoa anh cu");
                fileUploadService.deleteImage(existingUtility.getImageUrl());
            }
            // Upload ảnh mới
            String imagePath = fileUploadService.uploadImage(image);
            utilityDTO.setImageUrl(imagePath);
        } else {
            // Nếu không upload ảnh mới → giữ ảnh cũ
            utilityDTO.setImageUrl(existingUtility.getImageUrl());
        }
       Utility utility = utilityRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Utility is not found"));
       mapDTOToEntity(utilityDTO, utility);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        "Sửa tiện ích thành công", mapEntityToResponse(utilityRepository.save(utility)),
                        LocalDateTime.now()));
    }
    public ResponseEntity<ApiResponse> delete(Long id) {
        Utility existingUtility = utilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utility not found with id: " + id));
        if (existingUtility.getImageUrl() != null) {
        fileUploadService.deleteImage(existingUtility.getImageUrl());
        }   
        utilityRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(), "Xóa tiện ích thành công", null, LocalDateTime.now()));
    }

    public ResponseEntity<ApiResponse> updateUsed(Long id, UtilityUsed used) {
        Utility existingUtility = utilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utility not found with id: " + id));
        existingUtility.setIsUsed(used);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(), "Cập nhật tiện ích thành công", mapEntityToResponse(utilityRepository.save(existingUtility)), LocalDateTime.now()));
    }

    public ResponseEntity<ApiResponse> getByHotelId(Long hotelId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(),
                "Lấy tiện ích thành công",
                utilityRepository.findByHotel_Id(hotelId).stream().map(this::mapEntityToResponse).collect(Collectors.toList()),
                LocalDateTime.now()));
    }

    public ResponseEntity<ApiResponse> getByHotelIdAndType(Long hotelId, UtilityType type) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(),
                        "Lấy tiện ích thành công",
                        utilityRepository.findByHotel_IdAndTypeAndIsUsed(hotelId, type, UtilityUsed.USED).stream().map(this::mapEntityToResponse).collect(Collectors.toList()),
                        LocalDateTime.now()));
    }

    private UtilityDTO mapEntityToResponse(Utility utility) {
        return new UtilityDTO(
                utility.getId(),
                utility.getName(),
                utility.getImageUrl(),
                utility.getType(),
                utility.getHotel().getId(),
                utility.getPrice(),
                utility.getIsUsed()
        );
    }

    private void mapDTOToEntity(UtilityDTO utilityDTO, Utility utility) {
        Hotel hotel = hotelRepository.findById(utilityDTO.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        utility.setName(utilityDTO.getName());
        utility.setImageUrl(utilityDTO.getImageUrl());
        utility.setType(utilityDTO.getType());
        utility.setHotel(hotel);
        utility.setPrice(utilityDTO.getPrice());
    }




}
