package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.HotelDTO.HotelDTO;
import tdc.vn.managementhotel.dto.HotelDTO.HotelResponseDTO;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.Location;
import tdc.vn.managementhotel.entity.User;
import tdc.vn.managementhotel.repository.HotelRepository;
import tdc.vn.managementhotel.repository.LocationRepository;
import tdc.vn.managementhotel.repository.RoomRepository;
import tdc.vn.managementhotel.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    // Create
    public HotelResponseDTO createHotel(HotelDTO dto) {
        Hotel hotel = new Hotel();
        mapDtoToEntity(dto, hotel);
        return mapEntityToResponse(hotelRepository.save(hotel));
    }

    // Update
    public HotelResponseDTO updateHotel(Long id, HotelDTO dto) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        mapDtoToEntity(dto, hotel);
        return mapEntityToResponse(hotelRepository.save(hotel));
    }

    // Delete
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new EntityNotFoundException("Hotel not found");
        }
        hotelRepository.deleteById(id);
    }

    // Get by id
    public HotelResponseDTO getHotel(Long id) {
        return hotelRepository.findById(id)
                .map(this::mapEntityToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
    }

    // Get all
    public List<HotelResponseDTO> getAllHotels() {
        return hotelRepository.findAll()
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }
    // Get hotels by location
    public List<HotelResponseDTO> getHotelsByLocation(Long id) {
        return hotelRepository.findByLocationId(id)
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    // Map DTO → Entity
    private void mapDtoToEntity(HotelDTO dto, Hotel hotel) {
        hotel.setName(dto.getName());
        hotel.setAddress(dto.getAddress());
        hotel.setPhone(dto.getPhone());
        hotel.setImage(dto.getImage());
        hotel.setEmail(dto.getEmail());
        hotel.setStatus(dto.getStatus());

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        hotel.setLocation(location);

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        hotel.setUser(user);
    }

    // Map Entity → Response DTO
    private HotelResponseDTO mapEntityToResponse(Hotel hotel) {
        Map<String, BigDecimal> priceRange = roomRepository.findPriceRangeByHotelId(hotel.getId());

        return new HotelResponseDTO(
                hotel.getId(),
                hotel.getName(),
                hotel.getAddress(),
                hotel.getPhone(),
                hotel.getImage(),
                hotel.getEmail(),
                hotel.getStatus(),
                hotel.getLocation().getName(),
                priceRange.get("minPrice"),
                priceRange.get("maxPrice")
//                hotel.getUser().getUsername()
        );
    }
}