package tdc.vn.managementhotel.ControllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.UtilityDTO.UtilityDTO;
import tdc.vn.managementhotel.entity.Utility;
import tdc.vn.managementhotel.enums.UtilityType;
import tdc.vn.managementhotel.enums.UtilityUsed;
import tdc.vn.managementhotel.service.FileUploadService;
import tdc.vn.managementhotel.service.UtilityService;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("api/utility")
@RequiredArgsConstructor
public class UtilityController {
    private final UtilityService utilityService;
    private final FileUploadService fileUploadService;
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse> getUtilityByType(@PathVariable UtilityType type) {
        System.out.println(type);
        return utilityService.getByType(type);
    }
   
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createUtility(@ModelAttribute UtilityDTO utilityDTO, @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        System.out.println(utilityDTO);
        return utilityService.create(utilityDTO, image);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateUtility(
            @PathVariable Long id,
            @ModelAttribute UtilityDTO utilityDTO,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        return utilityService.update(id, utilityDTO, image);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUtility(@PathVariable Long id) {
        return utilityService.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUtilityById(@PathVariable Long id) {
        return utilityService.getById(id);
    }

    @GetMapping("/hotel/{id}")
    public ResponseEntity<ApiResponse> getUtilityByHotelId(@PathVariable Long id) {
        return utilityService.getByHotelId(id);
    }

    @GetMapping("/hotel/{id}/type/{type}")
    public ResponseEntity<ApiResponse>getUtilityByHotelIdAndType(@PathVariable Long id, @PathVariable UtilityType type) {
        return utilityService.getByHotelIdAndType(id, type);
    }

    @PutMapping("/{id}/used/{used}")
    public ResponseEntity<ApiResponse> updateUtilityUsed(@PathVariable Long id, @PathVariable UtilityUsed used) {
        return utilityService.updateUsed(id, used);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUtilities() {
        return utilityService.getAll();
    }
}
