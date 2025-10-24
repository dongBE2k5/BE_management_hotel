package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.config.ResourceNotFoundException;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.HotelDTO.HotelResponseDTO;
import tdc.vn.managementhotel.dto.LocationDTO.LocationResponseDTO;
import tdc.vn.managementhotel.dto.UtilityDTO.UtilityDTO;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.Location;
import tdc.vn.managementhotel.entity.Utility;
import tdc.vn.managementhotel.enums.UtilityType;
import tdc.vn.managementhotel.repository.LocationRepository;
import tdc.vn.managementhotel.repository.UtilityRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UtilityService {
    @Autowired
    private UtilityRepository utilityRepository;

    public ResponseEntity<ApiResponse> create(Utility utility) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        "Tạo tiện ích thành công",
                        utilityRepository.save(utility),
                        LocalDateTime.now()));
    }
    public ResponseEntity<ApiResponse> getByType(UtilityType type) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        "Lấy tiện ích thành công",
                        utilityRepository.findByType(type).stream().map(this::mapEntityToResponse),
                        LocalDateTime.now()));
    }
    public ResponseEntity<ApiResponse> update(Long id, Utility utilityDTO) {
//        Utility utilityEntity = utilityRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Utility is not found"));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        "Sửa tiện ích thành công",
                        utilityRepository.findById(id)
                                .map(this::mapEntityToResponse)
                                .orElseThrow(() -> new EntityNotFoundException("Location not found with id " + id)),
                        LocalDateTime.now()));
    }

    private UtilityDTO mapEntityToResponse(Utility utility) {
        return new UtilityDTO(
                utility.getId(),
                utility.getName(),
                utility.getImage(),
                utility.getType()
        );
    }




}
