package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.RoomDTO.RoomRequestDTO;
import tdc.vn.managementhotel.dto.VoucherDTO.VoucherRequestDTO;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.Room;
import tdc.vn.managementhotel.entity.TypeOfRoom;
import tdc.vn.managementhotel.entity.Voucher;
import tdc.vn.managementhotel.repository.VoucherRepository;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;

    public Voucher update(Long id, VoucherRequestDTO voucherDTO) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found with id: " + id));
        mapDtoToEntity(voucherDTO, voucher);
        return voucherRepository.save(voucher);
    }



    private void mapDtoToEntity(VoucherRequestDTO dto, Voucher voucher) {
        voucher.setCode(dto.getCode());
        voucher.setName(dto.getName());
        voucher.setDescription(dto.getDescription());
        voucher.setPriceCondition(dto.getPriceCondition());
        voucher.setHotelId(dto.getHotelId());
        voucher.setQuantity(dto.getQuantity());
        voucher.setPercent(dto.getPercent());
        voucher.setInitialQuantity(dto.getInitialQuantity());
        voucher.setActive(dto.isActive());
    }
}
