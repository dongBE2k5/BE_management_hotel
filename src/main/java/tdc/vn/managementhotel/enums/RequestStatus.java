package tdc.vn.managementhotel.enums;

public enum RequestStatus {
    SENT,
    RECEIVED,  // Lễ tân đã gửi yêu cầu kiểm tra phòng
    HAS_ISSUE,      // Nhân viên kiểm tra và phát hiện có vấn đề (đồ hư, mất,...)
    NO_ISSUE        // Nhân viên xác nhận phòng không có vấn đề gì
}
