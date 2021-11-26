package net.n2oapp.framework.autotest.application;

import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import static com.codeborne.selenide.Configuration.headless;

@TestPropertySource("classpath:net/n2oapp/framework/autotest/application/menu/application.properties")
public class MenuAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        headless = false;
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack());
    }

    @Test
    public void headerMenuItemWithIconAndBadgeTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/icon/app.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/icon/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/menu/header/icon/test.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("Хедер");
        page.header().nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = page.header().nav().anchor(0);
        menuItem.shouldHaveIcon();
        menuItem.iconShouldHaveClass("fa fa-bell");//fixme
        menuItem.shouldHaveBadge();
        menuItem.badgeShouldHaveValue("2");
    }
}
