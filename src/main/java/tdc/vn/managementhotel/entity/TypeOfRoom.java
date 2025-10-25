package tdc.vn.managementhotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.TypeRoom;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany(
            mappedBy = "typeOfRoom",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<TypeOfRoomItem> typeOfRoomItems = new HashSet<>();




}
