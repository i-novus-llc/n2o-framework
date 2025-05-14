package net.n2oapp.framework.sandbox.cases.websocket;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/websocket")
public class WebSocketMessageController {

    private final Random random = new Random();
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/{destination}/count")
    public String sendCount(@PathVariable String destination, HttpSession session) {
        BadgeMessage message = new BadgeMessage();
        Integer count = random.nextInt(14) + 1;
        message.setCount(count);
        messagingTemplate.convertAndSendToUser(session.getId(), destination, message);
        return "Сгенерированный счетчик баджа: " + count.toString();
    }

    @GetMapping("/{destination}/color")
    public String sendColor(@PathVariable String destination, HttpSession session) {
        BadgeMessage message = new BadgeMessage();
        int pick = random.nextInt(BadgeColorEnum.values().length);
        String color = BadgeColorEnum.values()[pick].toString();
        message.setColor(color);
        messagingTemplate.convertAndSendToUser(session.getId(), destination, message);
        return "Сгенерированный цвет баджа: " + "\"" + color + "\"";
    }
}
