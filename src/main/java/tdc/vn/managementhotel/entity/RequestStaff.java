package tdc.vn.managementhotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tdc.vn.managementhotel.enums.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "request_staff")
@AllArgsConstructor
public class RequestStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    private Long receiverId;
    private String content;


    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.SENT;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "requestStaff",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<RoomAssignment> roomAssignments;

    @OneToMany(mappedBy = "requestStaff",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<DamagedItem> damagedItems;


}
