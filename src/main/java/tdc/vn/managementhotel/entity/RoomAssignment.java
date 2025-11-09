package tdc.vn.managementhotel.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.AssignmentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "room_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // resquest
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_staff_id", nullable = false)
    @JsonIgnore
    private RequestStaff requestStaff;


    // resquest
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, referencedColumnName = "id")
    @JsonIgnore
    private Booking booking;

    // Phòng được giao dọn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonIgnore
    private Room room;

    // Nhân viên được giao
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private Employee employee;

    // Người lễ tân tạo yêu cầu (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private Employee createdBy;

    // Trạng thái nhiệm vụ: PENDING, IN_PROGRESS, DONE, CANCELLED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus status = AssignmentStatus.PENDING;

    // Thời gian giao nhiệm vụ
    private LocalDateTime assignedAt = LocalDateTime.now();

    // Khi nhân viên nhận nhiệm vụ
    private LocalDateTime acceptedAt;

    // Khi hoàn tất
    private LocalDateTime completedAt;

    // Ghi chú (nếu có)
    @Nullable
    private String note;
}
