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
        // Prefix cho messages gửi từ server -> client
        config.enableSimpleBroker("/topic");
        
        // Prefix cho messages client -> server
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint để client connect WebSocket
        registry.addEndpoint("/ws-chat")
                .setAllowedOrigins("http://localhost:3000") // Cho phép React connect
                .withSockJS(); // Fallback nếu browser không hỗ trợ WebSocket
    }
}
