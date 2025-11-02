package tdc.vn.managementhotel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.TypeOfRoomUtilityDTO.TypeOfRoomUtilityRequestDTO;
import tdc.vn.managementhotel.dto.TypeOfRoomUtilityDTO.TypeOfRoomUtilityResponseDTO;
import tdc.vn.managementhotel.entity.TypeOfRoom;
import tdc.vn.managementhotel.entity.TypeOfRoomUtility;
import tdc.vn.managementhotel.entity.Utility;
import tdc.vn.managementhotel.enums.UtilityType;
import tdc.vn.managementhotel.repository.HotelRepository;
import tdc.vn.managementhotel.repository.TypeOfRoomRepository;
import tdc.vn.managementhotel.repository.TypeOfRoomUtilityRepository;
import tdc.vn.managementhotel.repository.UtilityRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TypeOfRoomUtilityService {
    private final TypeOfRoomUtilityRepository typeOfRoomUtilityRepository;
    private final UtilityRepository utilityRepository;
    private final TypeOfRoomRepository typeOfRoomRepository;

    public ResponseEntity<ApiResponse> save(TypeOfRoomUtilityRequestDTO typeOfRoomUtilityDTO) {
        TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(typeOfRoomUtilityDTO.getTypeOfRoomId())
                .orElseThrow(() -> new RuntimeException("TypeOfRoom is not found"));
        Utility utility = utilityRepository.findById(typeOfRoomUtilityDTO.getUtilityId())
                .orElseThrow(() -> new RuntimeException("Utility is not found"));
        TypeOfRoomUtility typeOfRoomUtility = new TypeOfRoomUtility();
        typeOfRoomUtility.setTypeOfRoom(typeOfRoom);
        typeOfRoomUtility.setUtility(utility);
        TypeOfRoomUtility typeOfRoomUtilityRes =  typeOfRoomUtilityRepository.save(typeOfRoomUtility);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(), "Thêm thành công", mapEntityToItem(typeOfRoomUtilityRes), LocalDateTime.now()));
    }

    public ResponseEntity<ApiResponse> getAllByTypeOfRoomIdAndHotelIdAndUtilityType(Long typeOfRoomId, Long hotelId, UtilityType utilityType) {
        List<TypeOfRoomUtility> typeOfRoomUtilities = typeOfRoomUtilityRepository.findByTypeOfRoom_IdAndUtility_TypeAndUtility_Hotel_Id(typeOfRoomId, utilityType, hotelId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(), "Lấy dữ liệu thành công", mapEntityToResponse(typeOfRoomUtilities), LocalDateTime.now()));
    }

    public ResponseEntity<ApiResponse> getAllByHotelIdAndUtilityType(Long hotelId, UtilityType utilityType) {
        List<TypeOfRoomUtility> typeOfRoomUtilities = typeOfRoomUtilityRepository.findByUtility_Hotel_IdAndUtility_Type(hotelId, utilityType);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(), "Lấy dữ liệu thành công", mapEntityToResponse(typeOfRoomUtilities), LocalDateTime.now()));
    }

    public ResponseEntity<ApiResponse> getAllByHotelId(Long hotelId) {
        List<TypeOfRoomUtility> typeOfRoomUtilities = typeOfRoomUtilityRepository.findByUtility_Hotel_Id(hotelId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(), "Lấy dữ liệu thành công", mapEntityToResponse(typeOfRoomUtilities), LocalDateTime.now()));
    }

    public ResponseEntity<ApiResponse> update(Long id, TypeOfRoomUtilityRequestDTO[] typeOfRoomUtilityRequestDTOList) {
        System.out.println("id: " + id);
        System.out.println(Arrays.stream(typeOfRoomUtilityRequestDTOList).toList());
        List<TypeOfRoomUtility> typeOfRoomUtilities = typeOfRoomUtilityRepository.findByUtility_Id(id);
        typeOfRoomUtilityRepository.deleteAll(typeOfRoomUtilities);
        List<TypeOfRoomUtility> newTypeOfRoomUtilities = new ArrayList<>();
        if (typeOfRoomUtilityRequestDTOList != null) {
            for (TypeOfRoomUtilityRequestDTO typeOfRoomUtilityRequestDTO : typeOfRoomUtilityRequestDTOList) {
                TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(typeOfRoomUtilityRequestDTO.getTypeOfRoomId())
                        .orElseThrow(() -> new RuntimeException("TypeOfRoom is not found"));
                Utility utility = utilityRepository.findById(typeOfRoomUtilityRequestDTO.getUtilityId())
                        .orElseThrow(() -> new RuntimeException("Utility is not found"));
                TypeOfRoomUtility typeOfRoomUtility = new TypeOfRoomUtility();
                typeOfRoomUtility.setTypeOfRoom(typeOfRoom);
                typeOfRoomUtility.setUtility(utility);
                newTypeOfRoomUtilities.add(typeOfRoomUtility);
            }
        }
//        System.out.println(newTypeOfRoomUtilities);
        typeOfRoomUtilityRepository.saveAll(newTypeOfRoomUtilities);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(), "Cập nhật dữ liệu thành công", mapEntityToResponse(newTypeOfRoomUtilities), LocalDateTime.now()));
    }

    public ResponseEntity<ApiResponse> getByUtilityId(Long id) {
        List<TypeOfRoomUtility> typeOfRoomUtilities = typeOfRoomUtilityRepository.findByUtility_Id(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(), "Lấy dữ liệu thành công", mapEntityToResponse(typeOfRoomUtilities), LocalDateTime.now()));
    }
//    public String save(HotelUtilityRequestDTO hotelUtilityRequestDTO) {
//        System.out.println("Save hotel utility");
//        Hotel hotel = hotelRepository.findById(hotelUtilityRequestDTO.getHotelId())
//                .orElseThrow(() -> new ResourceNotFoundException("Hotel is not found"));
//        List<TypeOfRoomUtility> toSave = new ArrayList<>();
//        for (HotelUtilityRequestDTO.UtilityItem item : hotelUtilityRequestDTO.getUtilities()) {
//            Utility utility = utilityRepository.findById(item.getUtilityId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Utility is not found"));
//
//            TypeOfRoomUtility typeOfRoomUtility = new TypeOfRoomUtility();
//            typeOfRoomUtility.setHotel(hotel);
//            typeOfRoomUtility.setUtility(utility);
//            typeOfRoomUtility.setPrice(item.getPrice());
//
//            toSave.add(typeOfRoomUtility);
//        }
//        hotelUtilityRepository.saveAll(toSave);
//        return "Đã thêm thành công";
//    }
//
//    @Transactional
//    public ResponseEntity<ApiResponse> update(HotelUtilityRequestDTO hotelUtilityRequestDTO) {
//        Hotel hotel = hotelRepository.findById(hotelUtilityRequestDTO.getHotelId())
//                .orElseThrow(() -> new RuntimeException("Hotel not found"));
//
//        hotelUtilityRepository.deleteByHotel(hotel);
//
//        List<TypeOfRoomUtility> newUtilities = new ArrayList<>();
//        for (HotelUtilityRequestDTO.UtilityItem item : hotelUtilityRequestDTO.getUtilities()) {
//            Utility utility = utilityRepository.findById(item.getUtilityId())
//                    .orElseThrow(() -> new RuntimeException("Utility not found"));
//
//            TypeOfRoomUtility typeOfRoomUtility = new TypeOfRoomUtility();
//            typeOfRoomUtility.setHotel(hotel);
//            typeOfRoomUtility.setUtility(utility);
//            typeOfRoomUtility.setPrice(item.getPrice());
//
//            newUtilities.add(typeOfRoomUtility);
//        }
//
//        hotelUtilityRepository.saveAll(newUtilities);
//        Map<String, Object> data = new HashMap<>();
//        data.put("hotelId", hotel.getId());
//        data.put("utilitiesAdded", newUtilities.size());
//        data.put("hotelName", hotel.getName());
//
//        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
//                HttpStatus.CREATED.value(),
//                "Đã thêm " + newUtilities.size() + " tiện ích cho khách sạn " + hotel.getName(),
//                data,
//                LocalDateTime.now()
//        );
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    public ResponseEntity<ApiResponse> getUtilityByHotelIdAndUtilityType(Long hotelId, UtilityType utilityType) {
//        System.out.println("Hotel ID: " + hotelId);
//        List<TypeOfRoomUtility> listTypeOfRoomUtility = hotelUtilityRepository.findByHotel_IdAndUtility_Type(hotelId, utilityType);
//        System.out.println("Result size: " + (listTypeOfRoomUtility == null ? "null" : listTypeOfRoomUtility.size()));
//        assert listTypeOfRoomUtility != null;
//        if (listTypeOfRoomUtility.isEmpty()) {
//            throw new ResourceNotFoundException("Không tìm thấy tiện ích");
//        }
//        HotelUtilityResponseDTO hotelUtilityResponseList = new HotelUtilityResponseDTO();
//        hotelUtilityResponseList.setHotelId(listTypeOfRoomUtility.get(0).getHotel().getId());
//        System.out.println("is Null " + listTypeOfRoomUtility.get(0).getUtility().getName());
//
//        for (TypeOfRoomUtility typeOfRoomUtility : listTypeOfRoomUtility) {
//            HotelUtilityResponseDTO.UtilityItemResponse item = new HotelUtilityResponseDTO.UtilityItemResponse();
//            item.setId(typeOfRoomUtility.getUtility().getId());
//            item.setUtilityName(typeOfRoomUtility.getUtility().getName());
//            item.setPrice(typeOfRoomUtility.getPrice());
//
//            hotelUtilityResponseList.getUtilities().add(item); // ✅ Thêm vào danh sách
//        }
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new ApiResponse(HttpStatus.CREATED.value(),
//                        "Lấy dữ liệu thành công",
//                        hotelUtilityResponseList,
//                        LocalDateTime.now()));
//    }

    public TypeOfRoomUtilityResponseDTO mapEntityToResponse(List<TypeOfRoomUtility> entities) {
        TypeOfRoomUtilityResponseDTO dto = new TypeOfRoomUtilityResponseDTO();

        List<TypeOfRoomUtilityResponseDTO.UtilityItemResponse> utilities = entities.stream()
                .map(entity -> {
                    TypeOfRoomUtilityResponseDTO.UtilityItemResponse item = new TypeOfRoomUtilityResponseDTO.UtilityItemResponse();
                    item.setId(entity.getUtility().getId());
                    item.setUtilityName(entity.getUtility().getName());
                    item.setPrice(entity.getUtility().getPrice());
                    item.setTypeOfRoomId(entity.getTypeOfRoom().getId());
                    item.setTypeOfRoom(entity.getTypeOfRoom().getRoom());
                    item.setImageUrl(entity.getUtility().getImageUrl());
                    return item;
                })
                .collect(Collectors.toList());

            dto.setUtilities(utilities);
        return dto;
    }
    public TypeOfRoomUtilityResponseDTO.UtilityItemResponse mapEntityToItem(TypeOfRoomUtility entity) {
        TypeOfRoomUtilityResponseDTO.UtilityItemResponse item = new TypeOfRoomUtilityResponseDTO.UtilityItemResponse();
        item.setId(entity.getUtility().getId());
        item.setUtilityName(entity.getUtility().getName());
        item.setPrice(entity.getUtility().getPrice());
        item.setTypeOfRoomId(entity.getTypeOfRoom().getId());
        item.setTypeOfRoom(entity.getTypeOfRoom().getRoom());
        item.setImageUrl(entity.getUtility().getImageUrl());
        return item;
    }
}
