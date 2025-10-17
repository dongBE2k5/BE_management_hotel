package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tdc.vn.managementhotel.config.ResourceNotFoundException;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.HotelUtilityDTO.HotelUtilityRequestDTO;
import tdc.vn.managementhotel.dto.HotelUtilityDTO.HotelUtilityResponseDTO;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.HotelUtility;
import tdc.vn.managementhotel.entity.Utility;
import tdc.vn.managementhotel.enums.UtilityType;
import tdc.vn.managementhotel.repository.HotelRepository;
import tdc.vn.managementhotel.repository.HotelUtilityRepository;
import tdc.vn.managementhotel.repository.UtilityRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class HotelUtilityService {
    private final HotelUtilityRepository hotelUtilityRepository;
    private final HotelRepository hotelRepository;
    private final UtilityRepository utilityRepository;

    public String save(HotelUtilityRequestDTO hotelUtilityRequestDTO) {
        Hotel hotel = hotelRepository.findById(hotelUtilityRequestDTO.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel is not found"));
        List<HotelUtility> toSave = new ArrayList<>();
        for (HotelUtilityRequestDTO.UtilityItem item : hotelUtilityRequestDTO.getUtilities()) {
            Utility utility = utilityRepository.findById(item.getUtilityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utility is not found"));

            HotelUtility hotelUtility = new HotelUtility();
            hotelUtility.setHotel(hotel);
            hotelUtility.setUtility(utility);
            hotelUtility.setPrice(item.getPrice());

            toSave.add(hotelUtility);
        }
        return "Đã thêm thành công";
    }

    @Transactional
    public ResponseEntity<ApiResponse> update(HotelUtilityRequestDTO hotelUtilityRequestDTO) {
        Hotel hotel = hotelRepository.findById(hotelUtilityRequestDTO.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        hotelUtilityRepository.deleteByHotel(hotel);

        List<HotelUtility> newUtilities = new ArrayList<>();
        for (HotelUtilityRequestDTO.UtilityItem item : hotelUtilityRequestDTO.getUtilities()) {
            Utility utility = utilityRepository.findById(item.getUtilityId())
                    .orElseThrow(() -> new RuntimeException("Utility not found"));

            HotelUtility hotelUtility = new HotelUtility();
            hotelUtility.setHotel(hotel);
            hotelUtility.setUtility(utility);
            hotelUtility.setPrice(item.getPrice());

            newUtilities.add(hotelUtility);
        }

        hotelUtilityRepository.saveAll(newUtilities);
        Map<String, Object> data = new HashMap<>();
        data.put("hotelId", hotel.getId());
        data.put("utilitiesAdded", newUtilities.size());
        data.put("hotelName", hotel.getName());

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Đã thêm " + newUtilities.size() + " tiện ích cho khách sạn " + hotel.getName(),
                data,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<ApiResponse> getUtilityByHotelIdAndUtilityType(Long hotelId, UtilityType utilityType) {
        System.out.println("Hotel ID: " + hotelId);
        List<HotelUtility> listHotelUtility = hotelUtilityRepository.findByHotel_IdAndUtility_Type(hotelId, utilityType);
        System.out.println("Result size: " + (listHotelUtility == null ? "null" : listHotelUtility.size()));
        assert listHotelUtility != null;
        if (listHotelUtility.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy tiện ích");
        }
        HotelUtilityResponseDTO hotelUtilityResponseList = new HotelUtilityResponseDTO();
        hotelUtilityResponseList.setHotelId(listHotelUtility.get(0).getHotel().getId());
        System.out.println("is Null " + listHotelUtility.get(0).getUtility().getName());

        for (HotelUtility hotelUtility : listHotelUtility) {
            HotelUtilityResponseDTO.UtilityItemResponse item = new HotelUtilityResponseDTO.UtilityItemResponse();
            item.setUtilityName(hotelUtility.getUtility().getName());
            item.setPrice(hotelUtility.getPrice());

            hotelUtilityResponseList.getUtilities().add(item); // ✅ Thêm vào danh sách
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        "Lấy dữ liệu thành công",
                        hotelUtilityResponseList,
                        LocalDateTime.now()));
    }
}
