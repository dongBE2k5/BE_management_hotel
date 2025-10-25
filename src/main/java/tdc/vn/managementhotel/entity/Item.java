package tdc.vn.managementhotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="items")
public class Item {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(
            mappedBy = "item",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<TypeOfRoomItem> typeOfRoomItems = new HashSet<>();





}
