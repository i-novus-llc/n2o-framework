package net.n2oapp.framework.autotest.button;

import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для условия доступности кнопок
 */
public class ButtonEnabledAT extends AutoTestBase {

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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    @Test
    public void testEnabled() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/button/enabled/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        RadioGroup type = form.fields().field("type").control(RadioGroup.class);
        Toolbar toolbar = form.toolbar().bottomLeft();
        StandardButton button = toolbar.button("Button");
        DropdownButton subMenu = toolbar.dropdown();
        StandardButton item1 = subMenu.menuItem("item1");
        StandardButton item2 = subMenu.menuItem("item2");

        type.shouldBeEmpty();
        button.shouldBeDisabled();
        subMenu.click();
        item1.shouldBeDisabled();
        item2.shouldBeDisabled();

        type.check("button and all menu items");
        button.shouldBeEnabled();
        subMenu.click();
        item1.shouldBeEnabled();
        item2.shouldBeEnabled();

        type.check("first menu item");
        button.shouldBeDisabled();
        subMenu.click();
        item1.shouldBeEnabled();
        item2.shouldBeDisabled();

        type.check("second menu item");
        button.shouldBeDisabled();
        subMenu.click();
        item1.shouldBeDisabled();
        item2.shouldBeEnabled();

        type.check("none");
        button.shouldBeDisabled();
        subMenu.click();
        item1.shouldBeDisabled();
        item2.shouldBeDisabled();
    }

    @Test
    public void testEnabledDependency() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/button/enabled_dependency/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/enabled_dependency/test.object.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Условие доступности для кнопки");

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);
        StandardButton savingButton = form.toolbar().bottomLeft().button("Сохранить");
        savingButton.shouldBeEnabled();

        form = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class);
        StandardButton disabledButton = form.toolbar().bottomLeft().button("Подписать");
        disabledButton.shouldBeDisabled();

        form = page.regions().region(0, SimpleRegion.class).content().widget(2, FormWidget.class);
        form.shouldExists();

        savingButton.click();
        disabledButton.shouldBeDisabled();
    }
}
