package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.ImageRoomDTO.ImageRoomRequestDTO;
import tdc.vn.managementhotel.dto.ImageRoomDTO.ImageRoomResponseDTO;
import tdc.vn.managementhotel.dto.LocationDTO.LocationResponseDTO;
import tdc.vn.managementhotel.dto.RoomDTO.RoomRequestDTO;
import tdc.vn.managementhotel.dto.RoomDTO.RoomResponseDTO;
import tdc.vn.managementhotel.dto.TypeOfRoomDTO.TypeOfRoomDTO;
import tdc.vn.managementhotel.entity.*;
import tdc.vn.managementhotel.enums.TypeRoom;
import tdc.vn.managementhotel.repository.HotelRepository;
import tdc.vn.managementhotel.repository.ImageRoomRepository;
import tdc.vn.managementhotel.repository.LocationRepository;
import tdc.vn.managementhotel.repository.TypeOfRoomRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageRoomService {
    private final ImageRoomRepository imageRoomRepository;
    private final HotelRepository hotelRepository;
    private final TypeOfRoomRepository typeOfRoomRepository;

    public ResponseEntity<ApiResponse> create(ImageRoomRequestDTO imageRoomRequestDTO) {
        List<ImageRoom> imageRoomList = new ArrayList<>();
        TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(imageRoomRequestDTO.getRoomTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Type of room not found"));
        System.out.println("hotelID = " + imageRoomRequestDTO.getHotelId());
        Hotel hotel = hotelRepository.findById(imageRoomRequestDTO.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotels not found"));
        imageRoomRequestDTO.getImage().forEach(image -> {
            ImageRoom imageRoom = new ImageRoom();
            imageRoom.setName(image);
            imageRoom.setRoomType(typeOfRoom);
            imageRoom.setHotel(hotel);
            imageRoomList.add(imageRoom);
        });
        List<ImageRoomResponseDTO> imageRoomListRes = imageRoomRepository.saveAll(imageRoomList).stream().map(this::mapEntityToResponse)
                .collect(Collectors.toList());
        ApiResponse<List<ImageRoomResponseDTO>> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Đã thêm thành công ",
                imageRoomListRes,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ImageRoomResponseDTO findById(Long id) {
        return imageRoomRepository.findById(id)
                .map(this::mapEntityToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id " + id));
    }

    public List<ImageRoomResponseDTO> findAll() {
        return imageRoomRepository.findAll()
                .stream()
                .map(this::mapEntityToResponse)
                .toList();
    }

//    public ResponseEntity<ApiResponse> findAllByHotelID(Long hotelID) {
//        List<ImageRoom> images = imageRoomRepository.findAllByHotelId(hotelID);
//        Map<TypeRoom, List<ImageRoom>> grouped = images.stream()
//                .collect(Collectors.groupingBy(img -> img.getRoomType().getRoom()));
//        grouped.forEach((type, imgs) -> {
//            System.out.println("Loại phòng: " + type);
//            imgs.forEach(i -> System.out.println("  → " + i.getName()));
//        });
//        ApiResponse<Map<TypeRoom, List<ImageRoom>> > response = new ApiResponse<>(
//                HttpStatus.CREATED.value(),
//                "Đã lấy danh sách thành công",
//                grouped,
//                LocalDateTime.now()
//        );
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//
//    }
    public ResponseEntity<ApiResponse> getImagesGroupedByType(Long hotelId) {
        List<ImageRoom> images = imageRoomRepository.findAllByHotelId(hotelId);

        // Nhóm ảnh theo loại phòng (TypeRoom)
        Map<TypeOfRoom, List<ImageRoom>> grouped = images.stream()
                .collect(Collectors.groupingBy(ImageRoom::getRoomType));

        // Chuyển từng nhóm thành TypeOfRoomDTO
        List<TypeOfRoomDTO> typeRoomList = grouped.entrySet().stream()
                .map(entry -> {
                    TypeOfRoom roomType = entry.getKey();
                    List<ImageRoom> imageList = entry.getValue();

                    TypeOfRoomDTO dto = new TypeOfRoomDTO();
                    dto.setId(roomType.getId());
                    dto.setRoom(roomType.getRoom());
                    dto.setImageRooms(
                            imageList.stream()
                                    .map(img -> new ImageRoomResponseDTO(
                                            img.getId(),
                                            img.getName(), // hoặc img.getImage() nếu field là image
                                            img.getRoomType().getId()
                                    ))
                                    .collect(Collectors.toList())
                    );
                    return dto;
                })
                .collect(Collectors.toList());

        ApiResponse<List<TypeOfRoomDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Đã lấy danh sách thành công",
                typeRoomList,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }


//    public ImageRoomResponseDTO update(Long id, ImageRoomRequestDTO imageRoomRequestDTO) {
//        TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(imageRoomRequestDTO.getRoomTypeId())
//                .orElseThrow(() -> new EntityNotFoundException("Type of room not found"));
//        Hotel hotel = hotelRepository.findById(imageRoomRequestDTO.getHotelId())
//                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
//        return imageRoomRepository.findById(id)
//                .map(imageRoom -> {
//                    imageRoom.setName(imageRoomRequestDTO.getImage());
//                    imageRoom.setRoomType(typeOfRoom);
//                    imageRoom.setHotel(hotel);
//                    return mapEntityToResponse(imageRoomRepository.save(imageRoom));
//                })
//                .orElseThrow(() -> new EntityNotFoundException("Location not found with id " + id));
//    }
    @Transactional
    public ResponseEntity<ApiResponse> update(Long hotelId, ImageRoomRequestDTO imageRoomRequestDTO) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(imageRoomRequestDTO.getRoomTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Type of room not found"));

        imageRoomRepository.deleteByHotelIdAndRoomType_Id(hotelId, imageRoomRequestDTO.getRoomTypeId());
        List<ImageRoom> imageRoomList = imageRoomRequestDTO.getImage().stream()
                .map(image -> {
                    ImageRoom imageRoom = new ImageRoom();
                    imageRoom.setName(image);
                    imageRoom.setRoomType(typeOfRoom);
                    imageRoom.setHotel(hotel);
                    return imageRoom;
                })
                .collect(Collectors.toList());

        List<ImageRoomResponseDTO> imageRoomListRes = imageRoomRepository.saveAll(imageRoomList)
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());

        ApiResponse<List<ImageRoomResponseDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Cập nhật thành công",
                imageRoomListRes,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<ApiResponse> delete(Long hotelId, Long typeRoomId) {
        imageRoomRepository.deleteByHotelIdAndRoomType_Id(hotelId, typeRoomId);
        ApiResponse<List<ImageRoomResponseDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Xóa thành công",
                new ArrayList<>(),
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }



    public List<ImageRoomResponseDTO> getByHotelID(Long hotelId) {
        return imageRoomRepository.findByHotelId(hotelId)
                .stream()
                .map(this::mapEntityToResponse)
                .toList();
    }

    public void delete(Long id) {
        if (!imageRoomRepository.existsById(id)) {
            throw new EntityNotFoundException("Location not found with id " + id);
        }
        imageRoomRepository.deleteById(id);
    }

//    private void mapDtoToEntity(ImageRoomRequestDTO dto, ImageRoom imageRoom) {
//        imageRoom.setName(dto.getImage());
////        imageRoom.setHotelId(dto.getHotelId());
////        imageRoom.setRoomTypeId(dto.getRoomTypeId());
//
//        // type of room
//        // hotel
//        TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(dto.getRoomTypeId())
//                .orElseThrow(() -> new EntityNotFoundException("Type of room not found"));
//        imageRoom.setRoomType(typeOfRoom);
//
//        Hotel hotel = hotelRepository.findById(dto.getHotelId())
//                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
//        imageRoom.setHotel(hotel);
//    }


    private ImageRoomResponseDTO mapEntityToResponse(ImageRoom imageRoom) {
        return new ImageRoomResponseDTO(
                imageRoom.getId(),
                imageRoom.getName(),
                imageRoom.getRoomType().getId()
        );
    }
}
