package net.n2oapp.framework.autotest.websocket;

import com.codeborne.selenide.Configuration;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Автотест отправки уведомлений в веб-сокетах
 */
public class WebSocketAT extends AutoTestBase {

    @Autowired
    private WebSocketMessageController webSocketMessageController;

    private SimplePage page;

    private static final String DESTINATION = "badge";


    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
        Configuration.timeout = 8000;
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Изменение счетчика в меню навигации по web-socket");
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oActionsPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/websocket/common/test.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/websocket/common/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/websocket/common/app.application.xml"));
    }

    @Test
    @Disabled
    public void testWebSocketCount() {
        AnchorMenuItem menuItem = page.header().nav().anchor(0);
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeText("1");

        int exceptedCount = 10;
        webSocketMessageController.sendCount(DESTINATION, exceptedCount);
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeText(String.valueOf(exceptedCount));

        exceptedCount = 7;
        webSocketMessageController.sendCount(DESTINATION, exceptedCount);
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeText(String.valueOf(exceptedCount));
    }

    @Test
    @Disabled
    public void testWebSocketColor() {
        AnchorMenuItem menuItem = page.header().nav().anchor(0);
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeColor("danger");

        BadgeColor exceptedColor = BadgeColor.primary;
        webSocketMessageController.sendColor(DESTINATION, exceptedColor);
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeColor(exceptedColor.toString());

        exceptedColor = BadgeColor.danger;
        webSocketMessageController.sendColor(DESTINATION, exceptedColor);
        menuItem.shouldHaveBadge();
        menuItem.shouldHaveBadgeColor(exceptedColor.toString());
    }

}
