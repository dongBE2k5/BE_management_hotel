package tdc.vn.managementhotel.ControllerAPI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.entity.Voucher;
import tdc.vn.managementhotel.repository.VoucherRepository;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
@CrossOrigin(origins = "*") // cho phép react native gọi
public class VoucherController {

    @Autowired
    private VoucherRepository voucherRepository;

    // Lấy tất cả voucher
    @GetMapping
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    // Lấy voucher theo hotelId
    @GetMapping("/hotel/{hotelId}")
    public List<Voucher> getVouchersByHotel(@PathVariable Long hotelId) {
        return voucherRepository.findByHotelId(hotelId);
    }

    // Thêm voucher mới
    @PostMapping
    public Voucher createVoucher(@RequestBody Voucher voucher) {
        return voucherRepository.save(voucher);
    }
}
