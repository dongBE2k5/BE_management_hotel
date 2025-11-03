package tdc.vn.managementhotel.ControllerAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.entity.UserVoucher;
import tdc.vn.managementhotel.entity.Voucher;
import tdc.vn.managementhotel.repository.UserVoucherRepository;
import tdc.vn.managementhotel.repository.VoucherRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user-vouchers")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class UserVoucherController {

    @Autowired
    private UserVoucherRepository repo;

    @Autowired
    private VoucherRepository voucherRepository; // 👈 thêm dòng này

    @PostMapping
    public UserVoucher saveVoucherForUser(@RequestBody UserVoucher uv) {
        System.out.println("📩 Nhận dữ liệu từ frontend: userId=" + uv.getUserId() + ", voucherId=" + uv.getVoucherId());

        // ✅ Kiểm tra xem user đã lưu voucher này chưa
        List<UserVoucher> existing = repo.findByUserIdAndVoucherId(uv.getUserId(), uv.getVoucherId());
        if (!existing.isEmpty()) {
            System.out.println("⚠️ Voucher này đã được lưu trước đó!");
            return existing.get(0); // Trả lại bản ghi cũ, không thêm mới
        }

        // ✅ Nếu chưa có, mới lưu
        UserVoucher saved = repo.save(uv);
        System.out.println("✅ Đã lưu vào DB với id=" + saved.getId());
        return saved;
    }



    @GetMapping("/{userId}")
    public List<Voucher> getUserVouchers(@PathVariable Long userId) {
        List<UserVoucher> userVouchers = repo.findByUserId(userId);
        List<Voucher> vouchers = new ArrayList<>();
        for (UserVoucher uv : userVouchers) {
            voucherRepository.findById(uv.getVoucherId()).ifPresent(vouchers::add);
        }
        return vouchers;
    }


    @DeleteMapping("/{id}")
    public void deleteUserVoucher(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
