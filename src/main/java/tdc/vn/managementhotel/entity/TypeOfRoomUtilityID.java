package tdc.vn.managementhotel.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfRoomUtilityID implements Serializable {
    private Long typeOfRoomId;
    private Long utilityId;
}
