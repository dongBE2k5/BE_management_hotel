package tdc.vn.managementhotel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.HotelDTO.HotelResponseDTO;
import tdc.vn.managementhotel.entity.ViewedHotel;
import tdc.vn.managementhotel.repository.ViewedHotelRepository;
import tdc.vn.managementhotel.repository.HotelRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViewedHotelService {
    private final ViewedHotelRepository viewedHotelRepository;
    private final HotelRepository hotelRepository;

    public void saveViewedHotel(Long userId, Long hotelId) {
        // ✅ Kiểm tra đã tồn tại bản ghi chưa
        Optional<ViewedHotel> existing = viewedHotelRepository.findByUserIdAndHotelId(userId, hotelId);

        if (existing.isPresent()) {
            // ✅ Nếu đã tồn tại → chỉ cập nhật thời gian
            ViewedHotel viewed = existing.get();
            viewed.setViewedAt(LocalDateTime.now());
            viewedHotelRepository.save(viewed);
        } else {
            // ✅ Nếu chưa có → thêm mới
            ViewedHotel viewed = ViewedHotel.builder()
                    .userId(userId)
                    .hotelId(hotelId)
                    .viewedAt(LocalDateTime.now())
                    .build();
            viewedHotelRepository.save(viewed);
        }
    }

    public List<HotelResponseDTO> getRecentlyViewedHotels(Long userId) {
        return viewedHotelRepository.findTop10ByUserIdOrderByViewedAtDesc(userId)
                .stream()
                .map(v -> hotelRepository.findById(v.getHotelId())
                        .map(HotelResponseDTO::new)
                        .orElse(null))
                .filter(h -> h != null)
                .collect(Collectors.toList());
    }
}