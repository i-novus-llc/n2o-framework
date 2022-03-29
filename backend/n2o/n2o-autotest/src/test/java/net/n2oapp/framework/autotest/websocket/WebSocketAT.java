package net.n2oapp.framework.autotest.websocket;

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
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        page = open(SimplePage.class);
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oActionsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/websocket/test.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/websocket/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/websocket/app.application.xml"));
    }

    //TODO: пофиксить моргание, добавить доп. проверки при загрузке страницы
    @Disabled
    @Test
    public void testWebSocketCount() {
        Integer exceptedCount = 10;
        AnchorMenuItem menuItem = page.header().nav().anchor(0);
        menuItem.shouldExists();
        menuItem.badgeShouldHaveValue("1");
        menuItem.badgeColorShouldHaveValue("danger");

        webSocketMessageController.sendCount(DESTINATION, exceptedCount);
        menuItem.badgeShouldHaveValue(exceptedCount.toString());

        exceptedCount = 7;
        webSocketMessageController.sendCount(DESTINATION, exceptedCount);
        menuItem.badgeShouldHaveValue(exceptedCount.toString());
    }

    @Disabled
    @Test
    public void testWebSocketColor()  {
        BadgeColor exceptedColor = BadgeColor.primary;
        AnchorMenuItem menuItem = page.header().nav().anchor(0);
        menuItem.shouldExists();
        menuItem.badgeShouldHaveValue("1");
        menuItem.badgeColorShouldHaveValue("danger");

        webSocketMessageController.sendColor(DESTINATION, exceptedColor);
        menuItem.badgeColorShouldHaveValue(exceptedColor.toString());

        exceptedColor = BadgeColor.danger;
        webSocketMessageController.sendColor(DESTINATION, exceptedColor);
        menuItem.badgeColorShouldHaveValue(exceptedColor.toString());
    }

}
