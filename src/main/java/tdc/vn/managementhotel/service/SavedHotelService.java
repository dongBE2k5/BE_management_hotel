package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.HotelDTO.HotelResponseDTO;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.SavedHotel;
import tdc.vn.managementhotel.repository.HotelRepository;
import tdc.vn.managementhotel.repository.SavedHotelRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavedHotelService {

    private final SavedHotelRepository savedHotelRepository;
    private final HotelRepository hotelRepository;

    //  Lưu khách sạn
    public void saveHotel(Long userId, Long hotelId) {
        boolean exists = savedHotelRepository.findByUserIdAndHotelId(userId, hotelId).isPresent();
        if (!exists) {
            SavedHotel savedHotel = new SavedHotel();
            savedHotel.setUserId(userId);
            savedHotel.setHotelId(hotelId);
            savedHotelRepository.save(savedHotel);
        }
    }

    //  Bỏ lưu
    public void removeHotel(Long userId, Long hotelId) {
        savedHotelRepository.findByUserIdAndHotelId(userId, hotelId)
                .ifPresent(savedHotelRepository::delete);
    }

    //  Lấy danh sách khách sạn đã lưu
    public List<HotelResponseDTO> getSavedHotels(Long userId) {
        List<SavedHotel> savedList = savedHotelRepository.findByUserId(userId);

        return savedList.stream()
                .map(item -> hotelRepository.findById(item.getHotelId())
                        .map(hotel -> new HotelResponseDTO(
                                hotel.getId(),
                                hotel.getName(),
                                hotel.getAddress(),
                                hotel.getPhone(),
                                hotel.getImage(),
                                hotel.getEmail(),
                                hotel.getStatus(),
                                hotel.getLocation().getName(),
                                hotel.getMinPrice(),
                                hotel.getMaxPrice()
                        ))
                        .orElse(null))
                .filter(h -> h != null)
                .collect(Collectors.toList());
    }
    // Kiểm tra khách sạn đã được lưu hay chưa
    public boolean isHotelSaved(Long userId, Long hotelId) {
        return savedHotelRepository.findByUserIdAndHotelId(userId, hotelId).isPresent();
    }

    //lay theo location
    public List<HotelResponseDTO> getSavedHotelsByLocation(Long userId, Long locationId) {
        List<SavedHotel> savedHotels = savedHotelRepository.findByUserIdAndLocationId(userId, locationId);

        return savedHotels.stream()
                // Lấy Entity Hotel
                .map(s -> hotelRepository.findById(s.getHotelId()).orElse(null))
                .filter(h -> h != null)
                // Ánh xạ Hotel Entity sang DTO để tránh LazyInitializationException
                .map(hotel -> new HotelResponseDTO(
                        hotel.getId(),
                        hotel.getName(),
                        hotel.getAddress(),
                        hotel.getPhone(),
                        hotel.getImage(),
                        hotel.getEmail(),
                        hotel.getStatus(),
                        // Chắc chắn Location đã được tải
                        hotel.getLocation().getName(),
                        hotel.getMinPrice(),
                        hotel.getMaxPrice()
                ))
                .collect(Collectors.toList());
    }

}
