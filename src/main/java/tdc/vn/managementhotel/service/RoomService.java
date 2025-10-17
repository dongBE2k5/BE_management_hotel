package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.HotelSummaryDTO;
import tdc.vn.managementhotel.dto.ImageRoomDTO.ImageRoomResponseDTO;
import tdc.vn.managementhotel.dto.RoomDTO.RoomRequestDTO;
import tdc.vn.managementhotel.dto.RoomDTO.RoomResponseDTO;
import tdc.vn.managementhotel.entity.*;
import tdc.vn.managementhotel.repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ImageRoomRepository imageRoomRepository;
    private final TypeOfRoomRepository typeOfRoomRepository;

    // Create
    public RoomResponseDTO createRoom(RoomRequestDTO dto) {
        Room room = new Room();
//        System.out.println(dto.getImageRoom());
        mapDtoToEntity(dto, room);
        RoomResponseDTO roomResponseDTO = mapEntityToResponse(roomRepository.save(room));

//        List<ImageRoom> listImage = dto.getImageRoom().stream()
//                .map(imageRoom -> {
//                    ImageRoom imageRoomItem = new ImageRoom();
//                    imageRoomItem.setName(imageRoom.getImage());
//                    imageRoomItem.setRoomId(roomResponseDTO.getId());
//                    return imageRoomItem;
//                })
//                .toList();
//        List<ImageRoom> savedImages = imageRoomRepository.saveAll(listImage);
        return roomResponseDTO;
    }

    // Update
    public RoomResponseDTO updateRoom(Long id, RoomRequestDTO dto) {
        // 1. Tìm room
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
//        List<ImageRoom> listRoom = imageRoomRepository.findByRoomId(id);
//        imageRoomRepository.deleteAll(listRoom);
        mapDtoToEntity(dto, room);
        Room updatedRoom = roomRepository.save(room);
//        List<ImageRoom> listImage = dto.getImageRoom().stream()
//                .map(imageRoom -> {
//                    ImageRoom imageRoomItem = new ImageRoom();
//                    imageRoomItem.setName(imageRoom.getImage());
//                    imageRoomItem.setRoomId(room.getId());
//                    return imageRoomItem;
//                })
//                .toList();
//
//        List<ImageRoom> savedImages = imageRoomRepository.saveAll(listImage);
        return mapEntityToResponse(updatedRoom);
    }

    // Delete
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new EntityNotFoundException("Room not found");
        }
        roomRepository.deleteById(id);
    }

    // Get by id
    public RoomResponseDTO getRoom(Long id) {
        return roomRepository.findById(id)
                .map(this::mapEntityToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
    }

    // Get all
    public List<RoomResponseDTO> getAllRooms() {
        System.out.println(roomRepository.findAll()
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList()));
        return roomRepository.findAll()
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }
    // Get rooms by hotel
    public List<RoomResponseDTO> getRoomsByHotel(Long id) {
        System.out.println(roomRepository.findByHotelId(id)
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList()));
        return roomRepository.findByHotelId(id)
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    public List<RoomResponseDTO> getRoomsAvailable(Long id, LocalDate checkInDate, LocalDate checkOutDate) {
        return roomRepository.findAvailableRooms(id, checkInDate, checkOutDate).stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    // Map DTO → Entity
    private void mapDtoToEntity(RoomRequestDTO dto, Room room) {
        room.setRoomNumber(dto.getRoomNumber());
        room.setDescription(dto.getDescription());
        room.setStatus(dto.getStatus());
        room.setPrice(dto.getPrice());
        // type of room
        // hotel
        TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(dto.getTypeRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Type of room not found"));
        room.setTypeOfRoom(typeOfRoom);

        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        room.setHotel(hotel);
    }

    // Map Entity → Response DTO
    private RoomResponseDTO mapEntityToResponse(Room room) {
//        List<ImageRoomResponseDTO> listRoom = new ArrayList<>();
//        room.getImageRoom().stream().map(imageRoom -> {
//            ImageRoomResponseDTO imageRoomResponseDTO = new ImageRoomResponseDTO();
//            imageRoomResponseDTO.setId(imageRoom.getId());
//            imageRoomResponseDTO.setImage(imageRoom.getName());
//            listRoom.add(imageRoomResponseDTO);
//            return listRoom;
//        }).toList();
        return new RoomResponseDTO(
                room.getId(),
                room.getRoomNumber(),
                room.getDescription(),
                room.getStatus(),
                room.getTypeOfRoom().getRoom(),
                room.getPrice(),
                new HotelSummaryDTO(room.getHotel().getId(), room.getHotel().getName()) // ✅ truyền object hotel
        );

    }
}