package net.n2oapp.framework.autotest.websocket;

import com.codeborne.selenide.Configuration;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.boot.stomp.N2oWebSocketController;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;


/**
 * Автотест отправки всплывающих уведомлений в веб-сокетах
 */
class WebSocketNotificationAT extends AutoTestBase {

    private static final String DESTINATION = "notifications";

    @Autowired
    private WebSocketMessageController webSocketMessageController;

    @Autowired
    private N2oWebSocketController wsController;

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
        Configuration.timeout = 8000;
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        MetadataEnvironment environment = builder.getEnvironment();
        wsController.setEnvironment(environment);
        wsController.setPipeline(N2oPipelineSupport.readPipeline(environment));
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oActionsPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/websocket/alert/test.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/websocket/alert/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/websocket/alert/app.application.xml"));
    }

    @Test
    @Disabled
    void testAlertNotification() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Отправка уведомлений по web-socket");

        Map<String, String> message = new HashMap<>();
        message.put("title", "Title");
        message.put("text", "Text");
        message.put("color", "primary");
        message.put("placement", "topLeft");

        webSocketMessageController.sendAlert(DESTINATION, message);
        page.shouldBeVisible();
        Alert alert = page.alerts(Alert.PlacementEnum.topLeft).alert(0);
        alert.shouldExists();
        alert.shouldHaveTitle("Title");
        alert.shouldHaveText("Text");
        alert.shouldHaveColor(ColorsEnum.PRIMARY);

        message.clear();

        message.put("title", "Hello world");
        message.put("text", "Привет Мир");
        message.put("color", "danger");
        message.put("placement", "bottomRight");

        webSocketMessageController.sendAlert(DESTINATION, message);
        page.shouldBeVisible();
        alert = page.alerts(Alert.PlacementEnum.bottomRight).alert(0);
        alert.shouldExists();
        alert.shouldHaveTitle("Hello world");
        alert.shouldHaveText("Привет Мир");
        alert.shouldHaveColor(ColorsEnum.DANGER);
    }
}
