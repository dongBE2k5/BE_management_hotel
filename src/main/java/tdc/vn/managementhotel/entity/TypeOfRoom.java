package tdc.vn.managementhotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.TypeRoom;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TypeRoom room;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="hotelId", nullable = false)
//    @JsonIgnore
//    private Hotel hotel;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageRoom> imageRooms;

}
