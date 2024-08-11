package com.saad.WebForum.config;

import com.saad.WebForum.model.ChatMessage;
import com.saad.WebForum.util.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @EventListener
    public void handleWebsocketDisconnectEventListener(
            SessionDisconnectEvent sessionDisconnectEvent
    ){
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        String username = (String) stompHeaderAccessor.getSessionAttributes().get("username");
        if(username != null){
            log.info("User disconnected : {}", username);
            ChatMessage chatMessage = ChatMessage.builder()
                    .messageType(MessageType.LEAVE)
                    .sender(username)
                    .build();
            simpMessageSendingOperations.convertAndSend("/topic/public",chatMessage);
        }
    }
}
