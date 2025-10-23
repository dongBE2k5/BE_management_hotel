package tdc.vn.managementhotel.ControllerAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.entity.Utility;
import tdc.vn.managementhotel.enums.UtilityType;
import tdc.vn.managementhotel.service.UtilityService;

@CrossOrigin
@RestController
@RequestMapping("api/utility")
@RequiredArgsConstructor
public class UtilityController {
    private final UtilityService utilityService;

    @GetMapping("/{type}")
    public ResponseEntity<ApiResponse> getUtilityByType(@PathVariable UtilityType type) {
        System.out.println(type);
        return utilityService.getByType(type);
    }
}
