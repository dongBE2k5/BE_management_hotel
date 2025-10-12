package tdc.vn.managementhotel.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tdc.vn.managementhotel.repository.BookingRepository;

import java.time.LocalDateTime;

@Component
public class BookingStatusScheduler {

    @Autowired
    private BookingRepository bookingRepository;

    // 🕒 Mỗi 1 phút kiểm tra các booking chưa thanh toán
//    @Scheduled(fixedRate = 3000)
//    @Transactional  // ✅ Quan trọng: đảm bảo có transaction cho update query
//    public void autoCancelUnpaidBookings() {
//        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(120);
//        bookingRepository.updateExpiredBookings(oneMinuteAgo);
//        System.out.println("✅ Đã tự động hủy các booking chưa thanh toán quá 1 phút.");
//    }
}
