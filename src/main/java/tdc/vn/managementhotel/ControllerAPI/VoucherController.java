package tdc.vn.managementhotel.ControllerAPI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.entity.Voucher;
import tdc.vn.managementhotel.repository.VoucherRepository;

import java.util.List;
import java.util.Optional;

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
        // Nếu initialQuantity chưa được set thì gán bằng quantity
        if (voucher.getInitialQuantity() == 0) {
            voucher.setInitialQuantity(voucher.getQuantity());
        }
        return voucherRepository.save(voucher);
    }

    // Sử dụng voucher: chỉ tăng used, không giảm quantity
    @PutMapping("/use/{id}")
    public ResponseEntity<?> useVoucher(
            @PathVariable Long id,
            @RequestParam double originalPrice) {

        Optional<Voucher> opt = voucherRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Voucher không tồn tại");
        }

        Voucher voucher = opt.get();

        // Kiểm tra giá gốc có đủ điều kiện áp voucher không
        if (originalPrice < voucher.getPriceCondition()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Giá chưa đủ điều kiện áp dụng voucher (tối thiểu: " + voucher.getPriceCondition() + ")");
        }

        // Kiểm tra số lượng còn lại
        if (voucher.getQuantity() <= 0 || !voucher.isActive()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Voucher đã hết lượt sử dụng hoặc không còn hiệu lực");
        }

        // Giảm quantity, tăng used
        voucher.setQuantity(voucher.getQuantity() - 1);
        voucher.setUsed(voucher.getUsed() + 1);

        // Nếu đã dùng hết => disable
        if (voucher.getQuantity() <= 0) {
            voucher.setActive(false);
        }

        voucherRepository.save(voucher);
        return ResponseEntity.ok(voucher);
    }




}
