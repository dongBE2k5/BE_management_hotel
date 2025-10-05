package tdc.vn.managementhotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.TypeRoom;

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
}
