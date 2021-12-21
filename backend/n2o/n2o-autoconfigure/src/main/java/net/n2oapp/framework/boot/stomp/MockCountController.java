package net.n2oapp.framework.boot.stomp;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MockCountController { //fixme удалить после дебага на фронте

    @MessageMapping("/ws")
    @SendTo("/topic/notif/count")
    public Message message() {
        Message message = new Message();
        message.setCount();
        return message;
    }
}
