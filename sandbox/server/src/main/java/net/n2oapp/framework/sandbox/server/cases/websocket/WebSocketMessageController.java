package net.n2oapp.framework.sandbox.server.cases.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
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
        int pick = random.nextInt(BadgeColor.values().length);
        String color = BadgeColor.values()[pick].toString();
        message.setColor(color);
        messagingTemplate.convertAndSendToUser(session.getId(), destination, message);
        return "Сгенерированный цвет баджа: " + "\"" + color + "\"";
    }
}
