package tdc.vn.managementhotel.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtBlacklistService {

    // Lưu token đã bị logout
    private Set<String> blacklistedTokens = new HashSet<>();

    // Thêm token vào blacklist
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    // Kiểm tra token có bị blacklist chưa
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}