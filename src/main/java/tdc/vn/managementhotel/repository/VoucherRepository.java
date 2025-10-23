package tdc.vn.managementhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tdc.vn.managementhotel.entity.Voucher;


import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    List<Voucher> findByHotelId(Long hotelId); // Lấy voucher theo hotel nếu cần
    @Query("SELECT v FROM Voucher v JOIN UserVoucher uv ON v.id = uv.voucherId WHERE uv.userId = :userId")
    List<Voucher> findVouchersByUserId(@Param("userId") Long userId);



}
