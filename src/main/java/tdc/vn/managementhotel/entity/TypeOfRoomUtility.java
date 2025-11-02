package tdc.vn.managementhotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfRoomUtility extends BaseEntity {

    @EmbeddedId
    private TypeOfRoomUtilityID id = new TypeOfRoomUtilityID();
    @ManyToOne
    @MapsId("typeOfRoomId")
    @JoinColumn(name = "type_of_room_id")
    private TypeOfRoom typeOfRoom;
    @ManyToOne
    @MapsId("utilityId")
    @JoinColumn(name = "utility_id")
    private Utility utility;
}
