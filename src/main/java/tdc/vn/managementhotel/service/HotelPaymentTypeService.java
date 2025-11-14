package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.HotelPaymentTypeDTO.HotelPaymentTypeRequestDTO;
import tdc.vn.managementhotel.dto.HotelPaymentTypeDTO.HotelPaymentTypeResponseDTO;
import tdc.vn.managementhotel.entity.Hotel;
import tdc.vn.managementhotel.entity.HotelPaymentType;
import tdc.vn.managementhotel.entity.PaymentTypes;
import tdc.vn.managementhotel.entity.TypeOfRoom;
import tdc.vn.managementhotel.repository.HotelPaymentTypeRepository;
import tdc.vn.managementhotel.repository.HotelRepository;
import tdc.vn.managementhotel.repository.PaymentTypeRepository;
import tdc.vn.managementhotel.repository.TypeOfRoomRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelPaymentTypeService {
//    private final HotelPaymentTypeRepository hotelPaymentTypeRepository;
//    private final HotelRepository hotelRepository;

    private final HotelPaymentTypeRepository hotelPaymentTypeRepository;
    private final HotelRepository hotelRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final TypeOfRoomRepository typeOfRoomRepository;

    public ResponseEntity<ApiResponse> save(HotelPaymentTypeRequestDTO dto) {

        // 1. Kiểm tra tồn tại hotel & payment type
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

        PaymentTypes paymentTypes = paymentTypeRepository.findById(dto.getPaymentTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Payment Type not found"));

        if (dto.getRoomTypeIds() == null || dto.getRoomTypeIds().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, "Vui lòng chọn ít nhất 1 loại phòng!", null, LocalDateTime.now()));
        }

        List<HotelPaymentType> savedList = new ArrayList<>();

        for (Long roomTypeId : dto.getRoomTypeIds()) {

            // 2. Kiểm tra loại phòng tồn tại
            TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(roomTypeId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy loại phòng với ID: " + roomTypeId));

            // 3. Kiểm tra duplicate theo (hotel + paymentType + roomType)
            boolean exists = hotelPaymentTypeRepository
                    .existsByHotelIdAndPaymentTypeIdAndTypeOfRoom_Id(dto.getHotelId(), dto.getPaymentTypeId(), roomTypeId);

            if (exists) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse(409,
                                "Phương thức thanh toán đã tồn tại cho loại phòng: " + typeOfRoom.getRoom(),
                                null, LocalDateTime.now()));
            }

            // 4. Tạo mới bản ghi
            HotelPaymentType hotelPaymentType = new HotelPaymentType();
            hotelPaymentType.setHotel(hotel);
            hotelPaymentType.setPaymentType(paymentTypes);
            hotelPaymentType.setTypeOfRoom(typeOfRoom);
            hotelPaymentType.setDepositPercent(dto.getDepositPercent());

            savedList.add(hotelPaymentType);
        }

        hotelPaymentTypeRepository.saveAll(savedList);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(201,
                        "Thêm phương thức thanh toán thành công",
                        savedList.stream().map(this::mapEntityToResponse).collect(Collectors.toList()),
                        LocalDateTime.now()));
    }

    public ResponseEntity<ApiResponse> findAllByHotelId(Long hotelId) {
        List<HotelPaymentType> records = hotelPaymentTypeRepository.findByHotelId(hotelId);

        Map<Double, List<HotelPaymentType>> grouped = records.stream()
                .collect(Collectors.groupingBy(HotelPaymentType::getDepositPercent));

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map.Entry<Double, List<HotelPaymentType>> entry : grouped.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("hotelId", hotelId);
            item.put("paymentType", records.get(0).getPaymentType().getPaymentType());
            item.put("depositPercent", entry.getKey());
            item.put("roomTypeIds", entry.getValue().stream()
                    .map(r -> r.getTypeOfRoom().getId())
                    .collect(Collectors.toList())
            );

            result.add(item);
        }

        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(),
                        "Lấy danh sách thành công",
                        result,
                        LocalDateTime.now())
        );
    }

    public ResponseEntity<ApiResponse> findAllByHotelIdAndTypeOfRoom(Long hotelId, Long typeOfRoomId) {
        List<HotelPaymentType> records = hotelPaymentTypeRepository.findByHotelIdAndTypeOfRoomId( hotelId, typeOfRoomId);

        Map<Double, List<HotelPaymentType>> grouped = records.stream()
                .collect(Collectors.groupingBy(HotelPaymentType::getDepositPercent));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Double, List<HotelPaymentType>> entry : grouped.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", records.get(0).getId());
            item.put("hotelId", hotelId);
            item.put("paymentType", records.get(0).getPaymentType().getPaymentType());
            item.put("depositPercent", entry.getKey());
            if (records.get(0).getId() != 1) {
                item.put("roomTypeIds", entry.getValue().stream()
                        .map(r -> r.getTypeOfRoom().getId())
                        .collect(Collectors.toList())
                );
            }
            item.put("roomTypeIds", entry.getValue().stream()
                    .map(r -> Optional.ofNullable(r.getTypeOfRoom())
                            .map(TypeOfRoom::getId)
                            .orElse(null))
                    .collect(Collectors.toList())
            );

            result.add(item);
        }

        return ResponseEntity.ok(
                new ApiResponse(HttpStatus.OK.value(),
                        "Lấy danh sách thành công",
                        result,
                        LocalDateTime.now())
        );
    }

    public ResponseEntity<ApiResponse> update(HotelPaymentTypeRequestDTO dto) {

        // 1. Kiểm tra tồn tại bản ghi gốc (để lấy hotel + paymentType đang cập nhật)
//        HotelPaymentType rootRecord = hotelPaymentTypeRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phương thức thanh toán!"));

        // 2. Validate Hotel & PaymentType mới
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hotel!"));

        PaymentTypes paymentTypes = paymentTypeRepository.findById(dto.getPaymentTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy loại thanh toán!"));

        if (dto.getRoomTypeIds() == null || dto.getRoomTypeIds().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, "Vui lòng chọn ít nhất 1 loại phòng!", null, LocalDateTime.now()));
        }

        // 3. Lấy toàn bộ bản ghi hiện tại của (hotel + paymentType) để cập nhật lại
        List<HotelPaymentType> oldRecords = hotelPaymentTypeRepository
                .findByHotelIdAndPaymentTypeId(dto.getHotelId(), dto.getPaymentTypeId());

        // 4. Xoá toàn bộ record cũ thuộc nhóm này (vì ta sẽ insert lại theo list mới)
        hotelPaymentTypeRepository.deleteAll(oldRecords);

        List<HotelPaymentType> newRecords = new ArrayList<>();

        // 5. Tạo mới theo danh sách roomTypeIds
        for (Long roomTypeId : dto.getRoomTypeIds()) {

            TypeOfRoom typeOfRoom = typeOfRoomRepository.findById(roomTypeId)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy loại phòng: " + roomTypeId));

            // Kiểm tra trùng (hotel + paymentType + roomType)
            boolean exists = hotelPaymentTypeRepository
                    .existsByHotelIdAndPaymentTypeIdAndTypeOfRoom_Id(dto.getHotelId(), dto.getPaymentTypeId(), roomTypeId);

            if (exists) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        new ApiResponse(409,
                                "Phương thức thanh toán đã tồn tại cho loại phòng: " + typeOfRoom.getRooms(),
                                null,
                                LocalDateTime.now())
                );
            }

            HotelPaymentType newRecord = new HotelPaymentType();
            newRecord.setHotel(hotel);
            newRecord.setPaymentType(paymentTypes);
            newRecord.setTypeOfRoom(typeOfRoom);
            newRecord.setDepositPercent(dto.getDepositPercent());

            newRecords.add(newRecord);
        }

        hotelPaymentTypeRepository.saveAll(newRecords);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Cập nhật thành công",
                        newRecords.stream().map(this::mapEntityToResponse).collect(Collectors.toList()),
                        LocalDateTime.now()
                )
        );
    }

    public ResponseEntity<ApiResponse> delete(Long id) {
        HotelPaymentType hotelPaymentType = hotelPaymentTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phương thức thanh toán!"));

        hotelPaymentTypeRepository.delete(hotelPaymentType);

        return ResponseEntity.ok(new ApiResponse(
                200,
                "Xóa thành công",
                null,
                LocalDateTime.now()
        ));
    }

    private HotelPaymentTypeResponseDTO mapEntityToResponse(HotelPaymentType hotelPaymentType) {
        return new HotelPaymentTypeResponseDTO(
                hotelPaymentType.getId(),
                hotelPaymentType.getHotel().getId(),
                hotelPaymentType.getPaymentType().getPaymentType(),
                hotelPaymentType.getDepositPercent(),
                hotelPaymentType.getTypeOfRoom().getId()
        );
    }


//    public ResponseEntity<ApiResponse> save(HotelPaymentTypeRequestDTO hotelPaymentTypeDTO) {
//        HotelPaymentType hotelPaymentType = new HotelPaymentType();
//        mapDtoToEntity(hotelPaymentTypeDTO, hotelPaymentType);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new ApiResponse(HttpStatus.CREATED.value(),
//                        "Cập nhật thành công",
//                        mapEntityToResponse(hotelPaymentTypeRepository.save(hotelPaymentType)),
//                        LocalDateTime.now()));
//    }

//    public ResponseEntity<ApiResponse> update(Long id, HotelPaymentTypeRequestDTO hotelPaymentTypeDTO) {
//        HotelPaymentType hotelPaymentType = hotelPaymentTypeRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Hotel Payment Type not found"));
//        Hotel hotel = hotelRepository.findById(hotelPaymentTypeDTO.getHotelId())
//                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
//        hotelPaymentType.setHotel(hotel);
//        hotelPaymentType.setPaymentType(hotelPaymentTypeDTO.getPaymentType());
//        hotelPaymentType.setDepositPercent(hotelPaymentTypeDTO.getDepositPercent());
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new ApiResponse(HttpStatus.CREATED.value(),
//                        "Cập nhật thành công",
//                        mapEntityToResponse(hotelPaymentTypeRepository.save(hotelPaymentType)),
//                        LocalDateTime.now()));
//    }
//
//    public ResponseEntity<ApiResponse> delete(Long id) {
//        HotelPaymentType hotelPaymentType = hotelPaymentTypeRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Hotel Payment Type not found"));
//        hotelPaymentTypeRepository.delete(hotelPaymentType);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
//
//    public ResponseEntity<ApiResponse> findAllByHotelId(Long hotelId) {
//        List<HotelPaymentTypeResponseDTO> hotelPaymentTypeResponseDTOList = hotelPaymentTypeRepository.findByHotelId(hotelId).stream()
//                .map(this::mapEntityToResponse)
//                .collect(Collectors.toList());
//        if (hotelPaymentTypeResponseDTOList.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new ApiResponse(HttpStatus.CREATED.value(),
//                        "Lấy danh sách thành công thành công",
//                        hotelPaymentTypeResponseDTOList,
//                        LocalDateTime.now()));
//    }
//
//    public ResponseEntity<ApiResponse> getById(Long id) {
//        HotelPaymentType hotelPaymentType = hotelPaymentTypeRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Hotel Payment Type not found"));
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new ApiResponse(HttpStatus.CREATED.value(),
//                        "Lấy dữ liệu thành công",
//                        mapEntityToResponse(hotelPaymentType),
//                        LocalDateTime.now()));
//    }
//
//
//
//    private void mapDtoToEntity(HotelPaymentTypeRequestDTO dto, HotelPaymentType hotelPaymentType) {
//        Hotel hotel = hotelRepository.findById(dto.getHotelId())
//                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
//        hotelPaymentType.setPaymentType(dto.getPaymentType());
//        hotelPaymentType.setDepositPercent(dto.getDepositPercent());
//        hotelPaymentType.setHotel(hotel);
//    }
//
//    public HotelPaymentTypeResponseDTO mapEntityToResponse(HotelPaymentType hotelPaymentType) {
//        return new HotelPaymentTypeResponseDTO(
//                hotelPaymentType.getId(),
//                hotelPaymentType.getHotel().getId(),
//                hotelPaymentType.getPaymentType(),
//                hotelPaymentType.getDepositPercent()
//        );
//    }

}
