package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.LocationDTO.LocationResponseDTO;
import tdc.vn.managementhotel.entity.Location;
import tdc.vn.managementhotel.entity.Utility;
import tdc.vn.managementhotel.repository.LocationRepository;
import tdc.vn.managementhotel.repository.UtilityRepository;

import java.util.List;

@Service
public class UtilityService {
    @Autowired
    private UtilityRepository utilityRepository;

    public Utility create(Utility utility) {
        return utilityRepository.save(utility);
    }




}
