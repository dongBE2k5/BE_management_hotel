package tdc.vn.managementhotel.entity;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor

public class RoomTypeItemID implements Serializable {
    private int typeOfRoomId;
    private int itemId;
}
