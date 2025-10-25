package tdc.vn.managementhotel.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "type_of_room_item")
public class TypeOfRoomItem {

     @EmbeddedId
     private RoomTypeItemID id = new RoomTypeItemID();

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @MapsId("typeOfRoomId")
    @JoinColumn(name = "type_of_room_id", nullable = false)
    private TypeOfRoom typeOfRoom;

    private int quantity;

    private BigDecimal price;




}
