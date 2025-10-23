package tdc.vn.managementhotel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Bật WebSocket cho Spring
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Đây là các "topic" mà client sẽ lắng nghe
        // Ví dụ: /topic/greetings, /topic/news
        config.enableSimpleBroker("/topic");

        // Đây là tiền tố cho các message đi từ client -> server
        // Ví dụ: client gửi đến /app/hello
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Đây là endpoint mà client sẽ kết nối tới
        // SockJS là một fallback cho trình duyệt không hỗ trợ WebSocket
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // Cho phép tất cả các domain kết nối
                .withSockJS();
    }
}
