package tdc.vn.managementhotel.config;

import okhttp3.*;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ZaloPayConfig {

    private static final String APP_ID = "2554"; // ví dụ sandbox
    private static final String KEY1 = "PcYkJvZOw8V6Dz3m"; // Lấy từ ZaloPay Merchant Portal
    private static final String CALLBACK_URL = "127.0.0.1 ";

    private final OkHttpClient client = new OkHttpClient();

    public String createOrder(long amount) throws Exception {
        String appUser = "demoUser";
        String appTransId = getAppTransId();
        long appTime = System.currentTimeMillis();

        // Tạo dữ liệu cho MAC
        String data = APP_ID + "|" + appTransId + "|" + appUser + "|" + amount + "|" + appTime
                + "|" + "Thanh toan don hang #" + appTransId + "|" + CALLBACK_URL;

        String mac = hmacSHA256(data, KEY1);

        // Tạo JSON body
        String orderJson = "{"
                + "\"app_id\": \"" + APP_ID + "\","
                + "\"app_user\": \"" + appUser + "\","
                + "\"app_trans_id\": \"" + appTransId + "\","
                + "\"app_time\": " + appTime + ","
                + "\"amount\": " + amount + ","
                + "\"description\": \"Thanh toan don hang #" + appTransId + "\","
                + "\"callback_url\": \"" + CALLBACK_URL + "\","
                + "\"mac\": \"" + mac + "\""
                + "}";

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, orderJson);

        Request request = new Request.Builder()
                .url("https://sb-openapi.zalopay.vn/v2/create")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("ZaloPay API error: " + response);
            }
            return response.body().string();
        }
    }

    private String getAppTransId() {
        // Format: yyMMdd_randomNumber
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        return sdf.format(new Date()) + "_" + (new Random().nextInt(900000) + 100000);
    }

    private String hmacSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        return bytesToHex(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) result.append(String.format("%02x", b));
        return result.toString();
    }
}
