package net.n2oapp.framework.sandbox.cases.websocket;

import jakarta.servlet.http.HttpSession;
import net.n2oapp.framework.boot.stomp.WebSocketController;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/websocket")
public class WebSocketMessageController {

    private final Random random = new Random();
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketController wsController;

    public WebSocketMessageController(SimpMessagingTemplate messagingTemplate, WebSocketController wsController) {
        this.messagingTemplate = messagingTemplate;
        this.wsController = wsController;
    }

    @GetMapping("/{destination}/count")
    public String sendCount(@PathVariable String destination, HttpSession session) {
        BadgeMessage message = new BadgeMessage();
        Integer count = random.nextInt(14) + 1;
        message.setCount(count);
        messagingTemplate.convertAndSendToUser(session.getId(), destination, message);
        return "Сгенерированный счетчик баджа: " + count;
    }

    @GetMapping("/{destination}/color")
    public String sendColor(@PathVariable String destination, HttpSession session) {
        BadgeMessage message = new BadgeMessage();
        int pick = random.nextInt(BadgeColorEnum.values().length);
        String color = BadgeColorEnum.values()[pick].toString().toLowerCase();
        message.setColor(color);
        messagingTemplate.convertAndSendToUser(session.getId(), destination, message);
        return "Сгенерированный цвет баджа: " + "\"" + color + "\"";
    }

    @GetMapping("/{destination}/alert")
    public Map<String, String> sendAlert(@PathVariable String destination, @RequestParam Map<String, String> message, HttpSession session) {
        wsController.convertAndSendToUser(session.getId(), destination, message);
        return message;
    }
}
