package tdc.vn.managementhotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.DamageStatus;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "damaged_item")
public class DamagedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonIgnore
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonIgnore
    private Room room;

    private int quantityAffected; // số lượng bị hư hoặc mất

    @Enumerated(EnumType.STRING)
    private DamageStatus status; // DAMAGED hoặc MISSING

    @Nullable
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id",nullable = false)
    @JsonIgnore
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_staff_id" ,nullable = false)
    @JsonIgnore
    private RequestStaff requestStaff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id" ,nullable = false)
    @JsonIgnore
    private Booking booking;

    private LocalDateTime reportedAt = LocalDateTime.now();
}
