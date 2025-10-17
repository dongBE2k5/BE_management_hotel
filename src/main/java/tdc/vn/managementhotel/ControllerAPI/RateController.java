package tdc.vn.managementhotel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdc.vn.managementhotel.dto.RateDTO;
import tdc.vn.managementhotel.entity.Rate;
import tdc.vn.managementhotel.service.RateService;

import java.util.List;

@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RateController {

    private final RateService rateService;

    @GetMapping("/hotel/{hotelId}")
    public List<RateDTO> getRatesByHotel(@PathVariable Long hotelId) {
        return rateService.getRatesByHotel(hotelId);
    }

    @PostMapping
    public Rate addRate(@RequestBody Rate rate) {
        return rateService.saveRate(rate);
    }

    @GetMapping("/hotel/{hotelId}/average")
    public Double getAverageRate(@PathVariable Long hotelId) {
        return rateService.getAverageRate(hotelId);
    }

    @GetMapping("/user/{userId}/room/{roomId}")
    public ResponseEntity<RateDTO> getRateByUserAndRoom(
            @PathVariable Long userId,
            @PathVariable Long roomId) {
        RateDTO rate = rateService.getRateByUserAndRoom(userId, roomId);
        return ResponseEntity.ok(rate);
    }




}
