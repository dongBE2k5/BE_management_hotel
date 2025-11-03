package tdc.vn.managementhotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import tdc.vn.managementhotel.enums.UtilityType;
import tdc.vn.managementhotel.enums.UtilityUsed;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utility extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private UtilityType type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private UtilityUsed isUsed = UtilityUsed.USED;
    // @OneToMany(mappedBy = "utility", cascade = CascadeType.ALL, orphanRemoval = true)
    // private Set<HotelUtility> hotelUtilities = new HashSet<>();

    @OneToMany(mappedBy = "utility", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TypeOfRoomUtility> typeOfRoomUtilities = new ArrayList<>();
}
