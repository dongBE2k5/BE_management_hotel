package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.ImageRoomDTO.ImageRoomRequestDTO;
import tdc.vn.managementhotel.dto.ImageRoomDTO.ImageRoomResponseDTO;
import tdc.vn.managementhotel.dto.LocationDTO.LocationResponseDTO;
import tdc.vn.managementhotel.dto.RoomDTO.RoomRequestDTO;
import tdc.vn.managementhotel.dto.RoomDTO.RoomResponseDTO;
import tdc.vn.managementhotel.entity.*;
import tdc.vn.managementhotel.repository.HotelRepository;
import tdc.vn.managementhotel.repository.ImageRoomRepository;
import tdc.vn.managementhotel.repository.LocationRepository;
import tdc.vn.managementhotel.repository.TypeOfRoomRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageRoomService {
    private final ImageRoomRepository imageRoomRepository;
    private final HotelRepository hotelRepository;
    private final TypeOfRoomRepository typeOfRoomRepository;

    public ImageRoomResponseDTO create(ImageRoomRequestDTO imageRoomRequestDTO) {
        ImageRoom imageRoom = imageRoomRepository.save(new ImageRoom());
        return mapEntityToResponse(imageRoom);
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

    public ImageRoomResponseDTO update(Long id, ImageRoomRequestDTO imageRoomRequestDTO) {
        TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(imageRoomRequestDTO.getRoomTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Type of room not found"));
        Hotel hotel = hotelRepository.findById(imageRoomRequestDTO.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        return imageRoomRepository.findById(id)
                .map(imageRoom -> {
                    imageRoom.setName(imageRoomRequestDTO.getImage());
                    imageRoom.setRoomType(typeOfRoom);
                    imageRoom.setHotel(hotel);
                    return mapEntityToResponse(imageRoomRepository.save(imageRoom));
                })
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id " + id));
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

    private void mapDtoToEntity(ImageRoomRequestDTO dto, ImageRoom imageRoom) {
        imageRoom.setName(dto.getImage());
//        imageRoom.setHotelId(dto.getHotelId());
//        imageRoom.setRoomTypeId(dto.getRoomTypeId());

        // type of room
        // hotel
        TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(dto.getRoomTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Type of room not found"));
        imageRoom.setRoomType(typeOfRoom);

        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        imageRoom.setHotel(hotel);
    }


    private ImageRoomResponseDTO mapEntityToResponse(ImageRoom imageRoom) {
        return new ImageRoomResponseDTO(
                imageRoom.getId(),
                imageRoom.getName(),
                imageRoom.getRoomType().getId()
        );
    }
}
