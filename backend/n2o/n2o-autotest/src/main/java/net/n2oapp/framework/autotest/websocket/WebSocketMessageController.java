package net.n2oapp.framework.autotest.websocket;

import net.n2oapp.framework.boot.stomp.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

public class WebSocketMessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private WebSocketController wsController;

    @Value("${n2o.stomp.user-id}")
    private String userId;

    public void sendCount(String destination, Integer count) {
        BadgeMessage message = new BadgeMessage();
        message.setCount(count);
        messagingTemplate.convertAndSendToUser(userId, destination, message);
    }

    public void sendColor(String destination, BadgeColor color) {
        BadgeMessage message = new BadgeMessage();
        message.setColor(color.toString());
        messagingTemplate.convertAndSendToUser(userId, destination, message);
    }

    public void sendAlert(String destination, Map<String, String> message) {
        wsController.convertAndSendToUser(userId, destination, message);
    }
}
