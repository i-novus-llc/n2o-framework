package net.n2oapp.framework.boot.stomp;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.action.N2oAlertAction;
import net.n2oapp.framework.api.metadata.action.N2oSetValueAction;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.N2oAbstractEvent;
import net.n2oapp.framework.api.metadata.event.N2oStompEvent;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBasePage;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.pipeline.ReadPipeline;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.register.route.N2oRouter;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Реализация контроллера для отправки сообщений по web-socket
 */
public class N2oWebSocketController implements WebSocketController {

    private static final String APPLICATION_DEFAULT_NAME = "default";
    private ReadPipeline pipeline;
    private MetadataEnvironment environment;
    private ObjectMapper mapper;
    private MetadataRouter router;
    private DomainProcessor domainProcessor;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public N2oWebSocketController(ReadPipeline pipeline, MetadataEnvironment environment, ObjectMapper mapper) {
        this.pipeline = pipeline;
        this.environment = environment;
        this.mapper = mapper;
        this.router = new N2oRouter(environment, environment.getReadCompilePipelineFunction().apply(new N2oPipelineSupport(environment)));
        domainProcessor = new DomainProcessor(mapper);
    }

    public void setPipeline(ReadPipeline pipeline) {
        this.pipeline = pipeline;
    }

    public void setEnvironment(MetadataEnvironment environment) {
        this.environment = environment;
        this.router = new N2oRouter(environment, environment.getReadCompilePipelineFunction().apply(new N2oPipelineSupport(environment)));
    }

    @Override
    public void convertAndSend(String destination, Object message, String pageRoute) {
        destination = RouteUtil.normalize(destination);
        messagingTemplate.convertAndSend(destination, initAction(destination, message, pageRoute));
    }


    @Override
    public void convertAndSendToUser(String user, String destination, Object message, String pageRoute) {
        destination = RouteUtil.normalize(destination);
        messagingTemplate.convertAndSendToUser(user, destination, initAction(destination, message, pageRoute));
    }

    private AbstractAction<?, ?> initAction(String destination, Object message, String pageRoute) {
        N2oAction stompAction;
        CompileProcessor p = new N2oCompileProcessor(environment);
        if (pageRoute == null || pageRoute.isEmpty()) {
            N2oApplication application = getSourceApplication();
            stompAction = getStompAction(destination, application.getEvents());
            return p.compile(resolveLinks(stompAction, message), null);
        } else {
            CompileContext<Page, ?> context = router.get(pageRoute, Page.class, new HashMap<>());
            N2oBasePage sourcePage = pipeline.read().get(context.getSourceId(null), N2oBasePage.class);
            Page compilePage = p.getCompiled(context);
            PageScope pageScope = new PageScope();
            pageScope.setPageId(compilePage.getId());
            stompAction = getStompAction(destination, sourcePage.getEvents());
            return p.compile(resolveLinks(stompAction, message), context, pageScope);
        }
    }

    private N2oAction getStompAction(String destination, N2oAbstractEvent[] events) {
        if (destination == null)
            throw new N2oStompException("Не указано место назначения");
        if (events == null)
            throw new N2oStompException("В метаданных не найдены события");
        for (N2oAbstractEvent event : events) {
            if (event instanceof N2oStompEvent stompEvent && destination.equals(stompEvent.getDestination()))
                return stompEvent.getAction();
        }
        throw new N2oStompException(String.format("В метаданных не найдены события с указанным местом назначения %s", destination));
    }

    private Object resolveLinks(Object stompAction, Object message) {
        Map<String, Object> messageActionMap = mapper.convertValue(message, Map.class);
        if (stompAction instanceof N2oAlertAction alertAction) {
            alertAction.setTitle(resolveStringValueLink(alertAction.getTitle(), messageActionMap));
            alertAction.setText(resolveStringValueLink(alertAction.getText(), messageActionMap));
            alertAction.setColor(resolveStringValueLink(alertAction.getColor(), messageActionMap));
            alertAction.setPlacement(resolveStringValueLink(alertAction.getPlacement(), messageActionMap));
            alertAction.setTime(resolveStringValueLink(alertAction.getTime(), messageActionMap));
            alertAction.setTimeout(resolveStringValueLink(alertAction.getTimeout(), messageActionMap));
        } else if (stompAction instanceof N2oSetValueAction setValueAction) {
            convertValues(messageActionMap);
            setValueAction.setExpression(resolveObjectValueLink(setValueAction.getExpression(), messageActionMap));
        }
        return stompAction;
    }

    private String resolveStringValueLink(String value, Map<String, Object> messageActionMap) {
        return resolvePlaceholders(value, messageActionMap, Object::toString);
    }

    private String resolveObjectValueLink(String value, Map<String, Object> messageActionMap) {
        return resolvePlaceholders(value, messageActionMap, v -> v instanceof String ? "'" + v + "'" : v.toString());
    }

    private String resolvePlaceholders(String value, Map<String, Object> messageActionMap,
                                       Function<Object, String> valueConverter) {
        if (!StringUtils.hasLink(value)) {
            return value;
        }
        String result = value;
        for (Map.Entry<String, Object> entry : messageActionMap.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            if (result.contains(placeholder)) {
                result = result.replace(placeholder, valueConverter.apply(entry.getValue()));
            }
        }
        return result;
    }

    private N2oApplication getSourceApplication() {
        return pipeline.read().get(getApplicationId(environment), N2oApplication.class);
    }

    private String getApplicationId(MetadataEnvironment environment) {
        List<SourceInfo> sourceInfos = environment.getMetadataRegister().find(N2oApplication.class);
        if (sourceInfos == null || sourceInfos.isEmpty())
            return APPLICATION_DEFAULT_NAME;
        return sourceInfos.stream()
                .map(SourceInfo::getId)
                .filter(s -> !APPLICATION_DEFAULT_NAME.equals(s)).findFirst().orElse(APPLICATION_DEFAULT_NAME);
    }

    private void convertValues(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            entry.setValue(domainProcessor.deserialize(entry.getValue()));
        }
    }

}
