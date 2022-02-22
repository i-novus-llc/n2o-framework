package net.n2oapp.framework.boot.stomp;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.application.N2oAbstractEvent;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.application.N2oStompEvent;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.pipeline.ReadPipeline;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Map;

/**
 * Реализация контроллера для отправки сообщений по web-socket
 */
public class N2oWebSocketController implements WebSocketController {

    private ReadPipeline pipeline;
    private MetadataEnvironment environment;
    private ObjectMapper mapper;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public N2oWebSocketController(ReadPipeline pipeline, MetadataEnvironment environment, ObjectMapper mapper) {
        this.pipeline = pipeline;
        this.environment = environment;
        this.mapper = mapper;
    }

    public void setPipeline(ReadPipeline pipeline) {
        this.pipeline = pipeline;
    }

    public void setEnvironment(MetadataEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void convertAndSend(String destination, Object message) {
        messagingTemplate.convertAndSend(destination, initAction(destination, message));
    }


    @Override
    public void convertAndSendToUser(String user, String destination, Object message) {
        messagingTemplate.convertAndSendToUser(user, destination, initAction(destination, message));
    }

    private  AbstractAction<?, ?> initAction(String destination, Object message) {
        N2oApplication application = getSourceApplication();
        N2oAction stompAction = getStompAction(destination, application);
        CompileProcessor p = new N2oCompileProcessor(environment);
        return p.compile(resolveLinks(stompAction, message), null);
    }

    private Object resolveLinks(Object stompAction, Object message) {
        Map<String, String> sourceMap = mapper.convertValue(stompAction, Map.class);
        Map<String, Object> messageActionMap = mapper.convertValue(message, Map.class);
        for (Map.Entry<String, String> attr : sourceMap.entrySet()) {
            if (StringUtils.isLink(attr.getValue())) {
                String text = StringUtils.unwrapLink(attr.getValue());
                sourceMap.put(attr.getKey(), messageActionMap.get(text) != null ? messageActionMap.get(text).toString() : null);
            }
        }
        return mapper.convertValue(sourceMap, stompAction.getClass());
    }

    private N2oAction getStompAction(String destination, N2oApplication application) {
        if (destination == null)
            throw new N2oStompException("Не указано место назначения");
        if (application.getEvents() == null)
            throw new N2oStompException("В метаданной приложения не найдены события");
        for (N2oAbstractEvent event : application.getEvents()) {
            if (event instanceof N2oStompEvent && RouteUtil.normalize(destination).equals(((N2oStompEvent) event).getDestination()))
                return ((N2oStompEvent) event).getAction();
        }
        throw new N2oStompException(String.format("В метаданной приложения не найдены события с указанным местом назначения %s", destination));
    }

    private N2oApplication getSourceApplication() {
        return pipeline.read().get(getApplicationId(environment), N2oApplication.class);
    }

    private String getApplicationId(MetadataEnvironment environment) {
        List<SourceInfo> sourceInfos = environment.getMetadataRegister().find(N2oApplication.class);
        if (sourceInfos == null || sourceInfos.isEmpty())
            return "default";
        return sourceInfos.stream()
                .map(SourceInfo::getId)
                .filter(s -> !"default".equals(s)).findFirst().orElse("default");
    }

}
