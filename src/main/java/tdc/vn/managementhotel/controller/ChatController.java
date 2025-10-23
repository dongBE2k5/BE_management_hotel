package tdc.vn.managementhotel.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import tdc.vn.managementhotel.model.ChatMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ChatController {
	 @GetMapping("/chat")
	    public String home(){
	        return "chat";
	    }

    @MessageMapping("/sendMessage") // Client sẽ gửi tới /app/sendMessage
    @SendTo("/topic/messages")     // Server gửi lại tới tất cả client sub "/topic/messages"
    public ChatMessage sendMessage(ChatMessage message) {
        message.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        return message;
    }


}

