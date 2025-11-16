package tdc.vn.managementhotel.controllerAPI;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.ApiResponse;
import tdc.vn.managementhotel.dto.ItemDTO.ItemRequestDTO;
import tdc.vn.managementhotel.dto.ItemDTO.ItemResponseDTO;
import tdc.vn.managementhotel.dto.RoomItemDTO.RoomItemRequestDTO;
import tdc.vn.managementhotel.entity.Item;
import tdc.vn.managementhotel.repository.ItemRepository;
import tdc.vn.managementhotel.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class ItemControllerAPI {
    private final ItemRepository itemRepository;
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ApiResponse<ItemResponseDTO>> createRoomItems(@RequestBody ItemRequestDTO dto)
    {
        ItemResponseDTO item= itemService.createItem(dto);

        return ResponseEntity.ok(ApiResponse.success("Created is successfully",item));
    }

    @GetMapping("/{hotelId}/hotel")
    public  ResponseEntity<ApiResponse<List<ItemResponseDTO>>> getByHotel(@PathVariable("hotelId") Long hotelId)
    {
        List<ItemResponseDTO> items= itemService.getAllItemsByHotel(hotelId);
        return ResponseEntity.ok(ApiResponse.success("Found is successfully",items));
    }
}
