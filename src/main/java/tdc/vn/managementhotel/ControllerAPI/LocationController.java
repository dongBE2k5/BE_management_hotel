package tdc.vn.managementhotel.ControllerAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tdc.vn.managementhotel.dto.LocationDTO.LocationResponseDTO;
import tdc.vn.managementhotel.entity.Location;
import tdc.vn.managementhotel.service.LocationService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/locations")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationResponseDTO> create(@RequestBody Location location) {
        return ResponseEntity.ok(locationService.create(location));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> find(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<LocationResponseDTO>> findAll() {
        return ResponseEntity.ok(locationService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> update(@PathVariable Long id, @RequestBody Location location) {
        return ResponseEntity.ok(locationService.update(id, location));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        locationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
