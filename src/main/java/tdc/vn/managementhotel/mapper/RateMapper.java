package tdc.vn.managementhotel.mapper;

import tdc.vn.managementhotel.dto.RateDTO;
import tdc.vn.managementhotel.entity.Rate;

public class RateMapper {
    public static RateDTO toDTO(Rate rate) {
        return new RateDTO(
                rate.getId(),
                rate.getRateNumber(),
                rate.getLikedPoints(),
                rate.getComment(),
                rate.getRoom().getId(),              // ✅ roomId
                rate.getUser().getId(),
                rate.getUser().getUsername()
        );
    }

}
