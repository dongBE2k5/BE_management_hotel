package tdc.vn.managementhotel.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.StatusRoom;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomNumber;
    private String description;
    @Enumerated(EnumType.STRING)
    private StatusRoom status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_room_id", nullable = false)
    private TypeOfRoom typeOfRoom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;
    private BigDecimal price;


//    @OneToMany(mappedBy = "roomId", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ImageRoom> imageRoom = new ArrayList<>();

}
