package tdc.vn.managementhotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rateNumber; // Số sao đánh giá (1–5)

    @ElementCollection
    @CollectionTable(name = "rate_liked_points", joinColumns = @JoinColumn(name = "rate_id"))
    @Column(name = "liked_point")
    private List<String> likedPoints; // Những điều người dùng thích

    private String comment; // Cảm nhận người dùng


    // 🔹 Quan hệ với Room (mỗi rate gắn với 1 phòng cụ thể)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Room room;

    // 🔹 Người dùng đánh giá
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;
}
