package net.n2oapp.framework.boot.stomp;

import net.n2oapp.framework.api.metadata.pipeline.ReadPipeline;

public interface WebSocketController {

    void convertAndSend(String destination, Object message);

    void convertAndSendToUser(String user, String destination, Object message);

    void setPipeline(ReadPipeline pipeline);
}
