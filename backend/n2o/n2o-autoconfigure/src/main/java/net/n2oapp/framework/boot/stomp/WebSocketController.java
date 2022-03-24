package net.n2oapp.framework.boot.stomp;

/**
 * Контроллер для отправки сообщений по web-socket
 */
public interface WebSocketController {

    void convertAndSend(String destination, Object message);

    void convertAndSendToUser(String user, String destination, Object message);

}
