package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.HotelPaymentType.HotelPaymentTypeRequestDTO;
import tdc.vn.managementhotel.dto.HotelPaymentType.HotelPaymentTypeResponseDTO;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.HotelPaymentType;
import tdc.vn.managementhotel.repository.HotelPaymentTypeRepository;
import tdc.vn.managementhotel.repository.HotelRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelPaymentTypeService {
    private final HotelPaymentTypeRepository hotelPaymentTypeRepository;
    private final HotelRepository hotelRepository;

    public ResponseEntity<ApiResponse> save(HotelPaymentTypeRequestDTO hotelPaymentTypeDTO) {
        HotelPaymentType hotelPaymentType = new HotelPaymentType();
        mapDtoToEntity(hotelPaymentTypeDTO, hotelPaymentType);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        "Cập nhật thành công",
                        mapEntityToResponse(hotelPaymentTypeRepository.save(hotelPaymentType)),
                        LocalDateTime.now()));
    }

    public ResponseEntity<ApiResponse> update(Long id, HotelPaymentTypeRequestDTO hotelPaymentTypeDTO) {
        HotelPaymentType hotelPaymentType = hotelPaymentTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel Payment Type not found"));
        Hotel hotel = hotelRepository.findById(hotelPaymentTypeDTO.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        hotelPaymentType.setHotel(hotel);
        hotelPaymentType.setPaymentType(hotelPaymentTypeDTO.getPaymentType());
        hotelPaymentType.setDepositPercent(hotelPaymentTypeDTO.getDepositPercent());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        "Cập nhật thành công",
                        mapEntityToResponse(hotelPaymentTypeRepository.save(hotelPaymentType)),
                        LocalDateTime.now()));
    }

    public ResponseEntity<ApiResponse> delete(Long id) {
        HotelPaymentType hotelPaymentType = hotelPaymentTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel Payment Type not found"));
        hotelPaymentTypeRepository.delete(hotelPaymentType);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<ApiResponse> findAllByHotelId(Long hotelId) {
        List<HotelPaymentTypeResponseDTO> hotelPaymentTypeResponseDTOList = hotelPaymentTypeRepository.findByHotelId(hotelId).stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
        if (hotelPaymentTypeResponseDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        "Lấy danh sách thành công thành công",
                        hotelPaymentTypeResponseDTOList,
                        LocalDateTime.now()));
    }

    public ResponseEntity<ApiResponse> getById(Long id) {
        HotelPaymentType hotelPaymentType = hotelPaymentTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel Payment Type not found"));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        "Lấy dữ liệu thành công",
                        mapEntityToResponse(hotelPaymentType),
                        LocalDateTime.now()));
    }



    private void mapDtoToEntity(HotelPaymentTypeRequestDTO dto, HotelPaymentType hotelPaymentType) {
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        hotelPaymentType.setPaymentType(dto.getPaymentType());
        hotelPaymentType.setDepositPercent(dto.getDepositPercent());
        hotelPaymentType.setHotel(hotel);
    }

    public HotelPaymentTypeResponseDTO mapEntityToResponse(HotelPaymentType hotelPaymentType) {
        return new HotelPaymentTypeResponseDTO(
                hotelPaymentType.getId(),
                hotelPaymentType.getHotel().getId(),
                hotelPaymentType.getPaymentType(),
                hotelPaymentType.getDepositPercent()
        );
    }

}
