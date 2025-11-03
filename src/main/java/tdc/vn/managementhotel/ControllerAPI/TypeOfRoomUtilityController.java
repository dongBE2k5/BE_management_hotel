package tdc.vn.managementhotel.ControllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.TypeOfRoomUtilityDTO.TypeOfRoomUtilityRequestDTO;
import tdc.vn.managementhotel.enums.UtilityType;
import tdc.vn.managementhotel.service.TypeOfRoomUtilityService;

@RestController
@RequestMapping("/api/type-of-room-utility")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class TypeOfRoomUtilityController {
    private final TypeOfRoomUtilityService typeOfRoomUtilityService;


    @PostMapping()
    public ResponseEntity<ApiResponse> save(@RequestBody TypeOfRoomUtilityRequestDTO typeOfRoomUtilityRequestDTO) {
        return typeOfRoomUtilityService.save(typeOfRoomUtilityRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody TypeOfRoomUtilityRequestDTO[] typeOfRoomUtilityRequestDTOList) {
        return typeOfRoomUtilityService.update(id, typeOfRoomUtilityRequestDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAllByTypeOfRoomIdAndHotelIdAndUtilityType(@PathVariable Long id, @PathVariable Long hotelId, @PathVariable UtilityType utilityType) {
        return typeOfRoomUtilityService.getAllByTypeOfRoomIdAndHotelIdAndUtilityType(id, hotelId, utilityType);
    }

    @GetMapping("/hotel/{hotelId}/type/{utilityType}")
    public ResponseEntity<ApiResponse> getAllByHotelIdAndUtilityType(@PathVariable Long hotelId, @PathVariable UtilityType utilityType) {
        return typeOfRoomUtilityService.getAllByHotelIdAndUtilityType(hotelId, utilityType);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse> getAllByHotelId(@PathVariable Long hotelId) {
        return typeOfRoomUtilityService.getAllByHotelId(hotelId);
    }

    @GetMapping("/utility/{id}")
    public ResponseEntity<ApiResponse> getByUtilityId(@PathVariable Long id) {
        return typeOfRoomUtilityService.getByUtilityId(id);
    }
//
//    @PostMapping()
//    public String insert(@RequestBody HotelUtilityRequestDTO hotelUtilityRequestDTO) {
//           return hotelUtilityService.save(hotelUtilityRequestDTO);
//    }
//    @PutMapping
//    public ResponseEntity<ApiResponse> update(@RequestBody HotelUtilityRequestDTO hotelUtilityRequestDTO) {
//        return hotelUtilityService.update(hotelUtilityRequestDTO);
//    }
//
//    @GetMapping("/hotel/{hotelId}/utility/{utilityType}")
//    public ResponseEntity<ApiResponse> get(@PathVariable Long hotelId, @PathVariable UtilityType utilityType) {
//        System.out.println(ResponseEntity.ok(hotelUtilityService.getUtilityByHotelIdAndUtilityType(hotelId, utilityType)));
//        return hotelUtilityService.getUtilityByHotelIdAndUtilityType(hotelId, utilityType);
//    }
}
