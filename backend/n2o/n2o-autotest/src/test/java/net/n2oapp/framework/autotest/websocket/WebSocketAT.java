package net.n2oapp.framework.autotest.websocket;

import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WebSocketAT extends AutoTestBase {

    @Autowired
    private WebSocketMessageController webSocketMessageController;

    private static final String DESTINATION = "badge";

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oActionsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/websocket/test.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/websocket/index.page.xml"));
    }

    @Test
    public void testWebSocketCount() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/websocket/app.application.xml"));

        Integer exceptedCount = 10;
        SimplePage page = open(SimplePage.class);
        AnchorMenuItem menuItem = page.header().nav().anchor(0);

        webSocketMessageController.sendCount(DESTINATION, exceptedCount);
        menuItem.badgeShouldHaveValue(exceptedCount.toString());

        exceptedCount = 7;
        webSocketMessageController.sendCount(DESTINATION, exceptedCount);
        menuItem.badgeShouldHaveValue(exceptedCount.toString());
    }

    @Test
    public void testWebSocketColor() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/websocket/app.application.xml"));

        BadgeColor exceptedColor = BadgeColor.info;
        SimplePage page = open(SimplePage.class);
        AnchorMenuItem menuItem = page.header().nav().anchor(0);

        webSocketMessageController.sendColor(DESTINATION, exceptedColor);
        menuItem.badgeShouldHaveValue(exceptedColor.toString());

        exceptedColor = BadgeColor.info;
        webSocketMessageController.sendColor(DESTINATION, exceptedColor);
        menuItem.badgeShouldHaveValue(exceptedColor.toString());
    }

}
