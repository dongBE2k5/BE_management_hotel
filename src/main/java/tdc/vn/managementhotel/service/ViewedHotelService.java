package tdc.vn.managementhotel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.HotelDTO.HotelResponseDTO;
import tdc.vn.managementhotel.dto.LocationDTO.LocationResponseDTO;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.ViewedHotel;
import tdc.vn.managementhotel.repository.ViewedHotelRepository;
import tdc.vn.managementhotel.repository.HotelRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViewedHotelService {
    private final ViewedHotelRepository viewedHotelRepository;
    private final HotelRepository hotelRepository;

    public void saveViewedHotel(Long userId, Long hotelId) {
        // ✅ Lấy Hotel entity theo id
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        // ✅ Kiểm tra đã tồn tại bản ghi chưa
        Optional<ViewedHotel> existing = viewedHotelRepository.findByUserIdAndHotel(userId, hotel);

        if (existing.isPresent()) {
            // ✅ Nếu đã tồn tại → chỉ cập nhật thời gian
            ViewedHotel viewed = existing.get();
            viewed.setViewedAt(LocalDateTime.now());
            viewedHotelRepository.save(viewed);
        } else {
            // ✅ Nếu chưa có → thêm mới
            ViewedHotel viewed = ViewedHotel.builder()
                    .userId(userId)
                    .hotel(hotel) // 👈 dùng entity Hotel
                    .viewedAt(LocalDateTime.now())
                    .build();
            viewedHotelRepository.save(viewed);
        }
    }

    public List<HotelResponseDTO> getRecentlyViewedHotels(Long userId) {
        return viewedHotelRepository.findTop10ByUserIdOrderByViewedAtDesc(userId)
                .stream()
                .map(this::mapEntityToResponse) // 👈 lấy từ entity Hotel
                .collect(Collectors.toList());
    }

    public List<HotelResponseDTO> getRecentlyViewedHotelsByLocation(Long userId, Long locationId) {
        if (locationId == null) {
            return getRecentlyViewedHotels(userId);
        }
        return viewedHotelRepository.findRecentlyViewedByUserAndLocation(userId, locationId)
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    private HotelResponseDTO mapEntityToResponse(ViewedHotel viewedHotel) {
        return new HotelResponseDTO(
                viewedHotel.getHotel().getId(),
                viewedHotel.getHotel().getName(),
                viewedHotel.getHotel().getAddress(),
                viewedHotel.getHotel().getPhone(),
                viewedHotel.getHotel().getImage(),
                viewedHotel.getHotel().getEmail(),
                viewedHotel.getHotel().getStatus(),
                new LocationResponseDTO( viewedHotel.getHotel().getLocation().getId(), viewedHotel.getHotel().getLocation().getName()),
                BigDecimal.valueOf(0),
                BigDecimal.valueOf(0)
//                hotel.getUser().getUsername()
        );
    }
    private HotelResponseDTO mapEntityToResponse(Hotel hotel) {
        return new HotelResponseDTO(
                hotel.getId(),
                hotel.getName(),
                hotel.getAddress(),
                hotel.getPhone(),
                hotel.getImage(),
                hotel.getEmail(),
                hotel.getStatus(),
                new LocationResponseDTO(hotel.getLocation().getId(), hotel.getLocation().getName()),
                BigDecimal.valueOf(0),
                BigDecimal.valueOf(0)
//                hotel.getUser().getUsername()
        );
    }

}
