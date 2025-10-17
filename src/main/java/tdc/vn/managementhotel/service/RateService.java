package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.RateDTO;
import tdc.vn.managementhotel.entity.Rate;
import tdc.vn.managementhotel.enums.BookingStatus;
import tdc.vn.managementhotel.mapper.RateMapper;
import tdc.vn.managementhotel.repository.BookingRepository;
import tdc.vn.managementhotel.repository.RateRepository;
import tdc.vn.managementhotel.repository.RoomRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RateService {
    private final RateRepository rateRepository;
    private final BookingRepository bookingRepository;
    public List<RateDTO> getRatesByHotel(Long hotelId) {
        return rateRepository.findByHotelId(hotelId)
                .stream()
                .map(RateMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Double getAverageRate(Long hotelId) {
        List<Rate> rates = rateRepository.findByHotelId(hotelId);
        return rates.isEmpty() ? 0.0 :
                rates.stream().mapToInt(Rate::getRateNumber).average().orElse(0.0);
    }

    public Rate saveRate(Rate rate) {
        Long userId = rate.getUser().getId();
        Long roomId = rate.getRoom().getId();

        boolean hasPaidBooking = bookingRepository
                .existsByUser_IdAndRoom_IdAndStatus(userId, roomId, BookingStatus.DA_THANH_TOAN);

        if (!hasPaidBooking) {
            throw new IllegalStateException("Bạn chỉ có thể đánh giá sau khi đã thanh toán phòng.");
        }

        return rateRepository.save(rate);
    }


    public RateDTO getRateByUserAndRoom(Long userId, Long roomId) {
        return rateRepository.findByUser_IdAndRoom_Id(userId, roomId)
                .map(rate -> new RateDTO(
                        rate.getId(),
                        rate.getRateNumber(),
                        rate.getLikedPoints(),
                        rate.getComment(),
                        rate.getRoom().getId(),
                        rate.getUser().getId(),
                        rate.getUser().getUsername()
                ))
                .orElse(null); // ✅ Trả về null thay vì ném lỗi
    }



}
