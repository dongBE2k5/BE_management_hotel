package tdc.vn.managementhotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tdc.vn.managementhotel.dto.HostHotelDTO.HostHotelRequestDTO;
import tdc.vn.managementhotel.dto.HostHotelDTO.HostHotelResponseDTO;

import tdc.vn.managementhotel.entity.HostHotel;
import tdc.vn.managementhotel.entity.User;
import tdc.vn.managementhotel.enums.HostHotelStatus;
import tdc.vn.managementhotel.repository.HostHotelRepository;
import tdc.vn.managementhotel.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HostHotelService {

    private final HostHotelRepository hostHotelRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * ➕ Tạo hoặc cập nhật thông tin chủ khách sạn
     */
    @Transactional
    public HostHotelResponseDTO createOrUpdate(HostHotelRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng có ID: " + dto.getUserId()));

        // Tìm HostHotel theo user, nếu chưa có thì tạo mới
        HostHotel hostHotel = hostHotelRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    HostHotel newHost = new HostHotel();
                    newHost.setUser(user);
                    return newHost;
                });

        // Cập nhật thông tin từ DTO
        hostHotel.setStk(dto.getStk());
        hostHotel.setNganHang(dto.getNganHang());
        hostHotel.setChiNhanh(dto.getChiNhanh());
        hostHotel.setCccd(dto.getCccd());
        hostHotel.setCccdMatTruoc(dto.getCccdMatTruoc());
        hostHotel.setCccdMatSau(dto.getCccdMatSau());
        hostHotel.setGiayPhepKinhDoanh(dto.getGiayPhepKinhDoanh());
        hostHotel.setStatus(HostHotelStatus.PENDING);
        HostHotel saved = hostHotelRepository.save(hostHotel);

        Map<String,Object> payload = new HashMap<>();
        payload.put("type","NEWHOST");
        payload.put("message","Đã thêm thành công 1 chủ khách sạn");
        payload.put("data",mapToResponse(saved));
        simpMessagingTemplate.convertAndSend("/topic/hosts",payload);

        return mapToResponse(saved);
    }

    /**
     * 🔍 Lấy thông tin HostHotel theo userId
     */
    @Transactional(readOnly = true)
    public HostHotelResponseDTO getByUserId(Long userId) {
        HostHotel hostHotel = hostHotelRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thông tin HostHotel cho userId: " + userId));

        return mapToResponse(hostHotel);
    }
    public HostHotelResponseDTO updateStatus(Long userId, HostHotelStatus status) {
        HostHotel hostHotel = hostHotelRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thông tin HostHotel cho userId: " + userId));
        hostHotel.setStatus(status);
        HostHotel saved= hostHotelRepository.save(hostHotel);
        return mapToResponse(saved);
    }

    /**
     * 📋 Lấy danh sách tất cả HostHotel
     */
    @Transactional(readOnly = true)
    public List<HostHotelResponseDTO> getAll() {
        return hostHotelRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 🧩 Ánh xạ Entity → ResponseDTO
     */
    private HostHotelResponseDTO mapToResponse(HostHotel entity) {
        HostHotelResponseDTO dto = new HostHotelResponseDTO();
        dto.setUserId(entity.getUser().getId());
        dto.setStk(entity.getStk());
        dto.setNganHang(entity.getNganHang());
        dto.setChiNhanh(entity.getChiNhanh());
        dto.setCccd(entity.getCccd());
        dto.setCccdMatTruoc(entity.getCccdMatTruoc());
        dto.setCccdMatSau(entity.getCccdMatSau());
        dto.setGiayPhepKinhDoanh(entity.getGiayPhepKinhDoanh());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }
}

