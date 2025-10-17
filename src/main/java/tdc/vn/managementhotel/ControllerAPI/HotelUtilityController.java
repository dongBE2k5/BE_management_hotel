package tdc.vn.managementhotel.ControllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.HotelUtilityDTO.HotelUtilityRequestDTO;
import tdc.vn.managementhotel.dto.HotelUtilityDTO.HotelUtilityResponseDTO;
import tdc.vn.managementhotel.entity.HotelUtility;
import tdc.vn.managementhotel.enums.UtilityType;
import tdc.vn.managementhotel.service.HotelUtilityService;

import java.util.List;

@RestController
@RequestMapping("/api/hotel-utility")
@RequiredArgsConstructor
@CrossOrigin
public class HotelUtilityController {
    private final HotelUtilityService hotelUtilityService;

    @PostMapping()
    public String insert(@RequestBody HotelUtilityRequestDTO hotelUtilityRequestDTO) {
           return hotelUtilityService.save(hotelUtilityRequestDTO);
    }
    @PutMapping
    public ResponseEntity<ApiResponse> update(@RequestBody HotelUtilityRequestDTO hotelUtilityRequestDTO) {
        return hotelUtilityService.update(hotelUtilityRequestDTO);
    }

    @GetMapping("/hotel/{hotelId}/utility/{utilityType}")
    public ResponseEntity<ApiResponse> get(@PathVariable Long hotelId, @PathVariable UtilityType utilityType) {
        System.out.println(ResponseEntity.ok(hotelUtilityService.getUtilityByHotelIdAndUtilityType(hotelId, utilityType)));
        return hotelUtilityService.getUtilityByHotelIdAndUtilityType(hotelId, utilityType);
    }
}
