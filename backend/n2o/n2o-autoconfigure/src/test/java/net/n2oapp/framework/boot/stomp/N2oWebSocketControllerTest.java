package net.n2oapp.framework.boot.stomp;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertAction;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueAction;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.metadata.pack.N2oAllPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.N2oTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class N2oWebSocketControllerTest extends N2oTestBase {
    private N2oWebSocketController controller;
    private Message<?> sentMessage;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        builder.packs(new N2oAllPack(), new N2oApplicationPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/boot/stomp/null-values.application.xml"));
        builder.scan();
        controller = new N2oWebSocketController(N2oPipelineSupport.readPipeline(builder.getEnvironment()),
                builder.getEnvironment(), new ObjectMapper());
        ExecutorSubscribableChannel channel = new ExecutorSubscribableChannel();
        channel.subscribe(message -> sentMessage = message);
        ReflectionTestUtils.setField(controller, "messagingTemplate", new SimpMessagingTemplate(channel));
    }

    @Test
    void sendsAlertWithNullFields() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("caption", null);
        payload.put("text", null);

        controller.convertAndSend("/alert", payload);

        AlertAction action = (AlertAction) receivePayload();
        assertNull(((AlertActionPayload) action.getPayload()).getAlerts().getFirst().getTitle());
        assertNull(((AlertActionPayload) action.getPayload()).getAlerts().getFirst().getText());
    }

    @Test
    void substitutesEmptyStringForNullInsideAlertText() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("caption", null);
        payload.put("text", "body");

        controller.convertAndSend("/interpolated-alert", payload);

        AlertAction action = (AlertAction) receivePayload();
        assertEquals("Notification: ", ((AlertActionPayload) action.getPayload()).getAlerts().getFirst().getTitle());
        assertEquals("body", ((AlertActionPayload) action.getPayload()).getAlerts().getFirst().getText());
    }

    @Test
    void preservesNonNullAlertValues() {
        controller.convertAndSend("/interpolated-alert", Map.of("caption", "hello", "text", "body"));

        AlertAction action = (AlertAction) receivePayload();
        assertEquals("Notification: hello", ((AlertActionPayload) action.getPayload()).getAlerts().getFirst().getTitle());
        assertEquals("body", ((AlertActionPayload) action.getPayload()).getAlerts().getFirst().getText());
    }

    @ParameterizedTest
    @MethodSource("setValues")
    void preservesSetValueTypes(Object value, String expectedExpression) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("value", value);

        controller.convertAndSend("/set-value", payload);

        SetValueAction action = (SetValueAction) receivePayload();
        assertEquals(expectedExpression, action.getPayload().getSourceMapper());
    }

    private static Stream<Arguments> setValues() {
        return Stream.of(
                Arguments.of(null, "(function(){return null;}).call(this)"),
                Arguments.of("", "(function(){return null;}).call(this)"),
                Arguments.of("hello", "(function(){return 'hello';}).call(this)"),
                Arguments.of(12, "(function(){return 12;}).call(this)"),
                Arguments.of(true, "(function(){return true;}).call(this)"));
    }

    private Object receivePayload() {
        assertNotNull(sentMessage);
        return sentMessage.getPayload();
    }
}
