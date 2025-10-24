package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.ImageRoomDTO.ImageRoomResponseDTO;
import tdc.vn.managementhotel.dto.TypeOfRoomDTO.TypeOfRoomDTO;
import tdc.vn.managementhotel.entity.ImageRoom;
import tdc.vn.managementhotel.entity.TypeOfRoom;
import tdc.vn.managementhotel.repository.TypeOfRoomRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TypeOfRoomService {
    private final TypeOfRoomRepository typeOfRoomRepository;
    public ResponseEntity<ApiResponse> all() {
        List<TypeOfRoomDTO> typeOfRoomDTO = typeOfRoomRepository.findAll()
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());

        ApiResponse<List<TypeOfRoomDTO>> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Lấy danh sách thành công ",
                typeOfRoomDTO,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

//    public ResponseEntity<ApiResponse> findByHotelId(Long hotelId) {
//        List<TypeOfRoomDTO> typeOfRoomDTO = typeOfRoomRepository.findByHotel_Id(hotelId)
//                .stream()
//                .map(this::mapEntityToResponse)
//                .collect(Collectors.toList());
//        if(typeOfRoomDTO.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        ApiResponse<List<TypeOfRoomDTO>> response = new ApiResponse<>(
//                HttpStatus.CREATED.value(),
//                "Lấy danh sách thành công ",
//                typeOfRoomDTO,
//                LocalDateTime.now()
//        );
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//
//    }

    public TypeOfRoomDTO findID(Long id) {
        TypeOfRoom typeRoom = typeOfRoomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));

        List<ImageRoomResponseDTO> imageNames = typeRoom.getImageRooms().stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());


        return new TypeOfRoomDTO(
                typeRoom.getId(),
                typeRoom.getRoom(),
                imageNames
        );
    }

    private TypeOfRoomDTO mapEntityToResponse(TypeOfRoom typeOfRoom) {
        return new TypeOfRoomDTO(
                typeOfRoom.getId(),
                typeOfRoom.getRoom(),
                typeOfRoom.getImageRooms().stream()
                        .map(this::mapEntityToResponse)
                        .collect(Collectors.toList())
        );
    }

    private ImageRoomResponseDTO mapEntityToResponse(ImageRoom imageRoom) {
        return new ImageRoomResponseDTO(
                imageRoom.getId(),
                imageRoom.getName(),
                imageRoom.getRoomType().getId()
        );
    }
}
