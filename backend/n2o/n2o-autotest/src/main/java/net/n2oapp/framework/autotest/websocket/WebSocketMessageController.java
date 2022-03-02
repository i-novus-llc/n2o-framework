package net.n2oapp.framework.autotest.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketMessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendCount(String destination, Integer count) {
        BadgeMessage message = new BadgeMessage();
        message.setCount(count);
        messagingTemplate.convertAndSend(destination, message);
    }

    public void sendColor(String destination, BadgeColor color) {
        BadgeMessage message = new BadgeMessage();
        message.setColor(color.toString());
        messagingTemplate.convertAndSend(destination, message);
    }
}
