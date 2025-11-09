package tdc.vn.managementhotel.service;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tdc.vn.managementhotel.repository.BookingRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BookingStatusScheduler {

    @Autowired
    private BookingRepository bookingRepository;
    private final RoomService roomService;



//    @Scheduled(fixedRate = 3000)
//    @Transactional  // ✅ Quan trọng: đảm bảo có transaction cho update query
//    public void autoCancelUnpaidBookings() {
//        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusHours(12);
//        bookingRepository.updateExpiredBookings(oneMinuteAgo);
//    }
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Ho_Chi_Minh")
    public void autoChangStatusRoomToScheduled() {
        System.out.println("changeStatusRoomToSchedule");
        roomService.changeStatusRoomToSchedule();
    }
}
