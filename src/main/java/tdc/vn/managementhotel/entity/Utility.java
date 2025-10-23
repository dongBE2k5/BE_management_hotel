package tdc.vn.managementhotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.UtilityType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;
    @Enumerated(EnumType.STRING)
    private UtilityType type;
    @OneToMany(mappedBy = "utility", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HotelUtility> hotelUtilities = new HashSet<>();
}
