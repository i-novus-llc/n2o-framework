package net.n2oapp.framework.boot.stomp;

/**
 * Контроллер для отправки сообщений по web-socket
 */
public interface WebSocketController {

    default void convertAndSend(String destination, Object message) {
        convertAndSend(destination, message, null);
    }

    void convertAndSend(String destination, Object message, String pageRoute);

    default void convertAndSendToUser(String user, String destination, Object message) {
        convertAndSendToUser(user, destination, message, null);
    }


    void convertAndSendToUser(String user, String destination, Object message, String pageRoute);

}
