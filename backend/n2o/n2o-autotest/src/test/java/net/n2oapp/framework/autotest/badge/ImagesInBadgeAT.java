package net.n2oapp.framework.autotest.badge;

import com.codeborne.selenide.Configuration;
import net.n2oapp.framework.autotest.api.component.Badge;
import net.n2oapp.framework.autotest.api.component.DropDown;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ImagesInBadgeAT extends AutoTestBase {
    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        Configuration.headless=false;
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/images_in_badge/badge_in_selectors/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/images_in_badge/badge_in_selectors/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/images_in_badge/badge_in_selectors/tree.query.xml"));
    }

    @Test
    public void test() throws InterruptedException {
        SimplePage page = open(SimplePage.class);

        InputSelect inputSelect = page.widget(FormWidget.class).fields().field("Ввод с выпадающим списком").control(InputSelect.class);
        inputSelect.expand();
        inputSelect.shouldBeExpanded();
        DropDown dropdown = page.dropdown();
        dropdown.shouldExists();
        dropdown.shouldHaveItem(4);
        DropDown.DropDownItem item = dropdown.item(0);
        item.shouldHaveBadge();
        Badge badge = item.badge();
        badge.shouldHaveImage();
        badge.shouldBeShape("circle");
        badge.imageShouldBePosition("right");
        badge.imageShouldBeShape("square");
//        Thread.sleep(10000000);
    }
}
