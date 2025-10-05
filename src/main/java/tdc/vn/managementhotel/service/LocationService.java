package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.LocationDTO.LocationResponseDTO;
import tdc.vn.managementhotel.entity.Location;
import tdc.vn.managementhotel.repository.LocationRepository;

import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public LocationResponseDTO create(Location location) {
        return mapEntityToResponse(locationRepository.save(location));
    }

    public LocationResponseDTO findById(Long id) {
        return locationRepository.findById(id)
                .map(this::mapEntityToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id " + id));
    }

    public List<LocationResponseDTO> findAll() {
        return locationRepository.findAll()
                .stream()
                .map(this::mapEntityToResponse)
                .toList();
    }

    public LocationResponseDTO update(Long id, Location newLocation) {
        return locationRepository.findById(id)
                .map(location -> {
                    location.setName(newLocation.getName());
                    return mapEntityToResponse(locationRepository.save(location));
                })
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id " + id));
    }

    public void delete(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Location not found with id " + id);
        }
        locationRepository.deleteById(id);
    }

    private LocationResponseDTO mapEntityToResponse(Location location) {
        return new LocationResponseDTO(
                location.getId(),
                location.getName()
        );
    }
}
