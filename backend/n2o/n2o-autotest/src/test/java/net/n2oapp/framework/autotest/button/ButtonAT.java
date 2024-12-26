package net.n2oapp.framework.autotest.button;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.Tooltip;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.list.ListWidget;
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
 * Автотест для проверки кнопок
 */
public class ButtonAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        setJsonPath("net/n2oapp/framework/autotest/button/resolve_attributes");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/button/resolve_attributes/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/resolve_attributes/reports.query.xml"));
    }

    @Test
    public void testResolveAttributes() {
        ListWidget list = page.widget(ListWidget.class);
        DropdownButton subMenu = list.toolbar().topLeft().dropdown();
        subMenu.shouldExists();
        subMenu.shouldHaveItems(2);
        subMenu.shouldHaveLabel("Все");
        subMenu.shouldHaveColor(Colors.SUCCESS);
        subMenu.hover();
        Tooltip tooltip = subMenu.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText(new String[]{"text1"});

        StandardButton item1 = subMenu.menuItem("Непрочитанные");
        StandardButton item2 = subMenu.menuItem("Все");

        StandardButton button = list.toolbar().topLeft().button("All");
        button.shouldExists();
        button.shouldHaveColor(Colors.PRIMARY);
        button.hover();
        tooltip = button.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText(new String[]{"value1"});

        subMenu.click();
        item1.shouldExists();
        item1.click();
        subMenu.shouldHaveLabel("Непрочитанные");
        subMenu.shouldHaveColor(Colors.DANGER);
        subMenu.hover();
        tooltip = subMenu.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText(new String[]{"text2"});

        button = list.toolbar().topLeft().button("Unread");
        button.shouldExists();
        button.shouldHaveColor(Colors.WARNING);
        button.hover();
        tooltip = button.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText(new String[]{"value2"});

        subMenu.click();
        item2.shouldExists();
        item2.click();
        subMenu.shouldHaveLabel("Все");
        subMenu.shouldHaveColor(Colors.SUCCESS);
        subMenu.hover();
        tooltip = subMenu.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText(new String[]{"text1"});

        button = list.toolbar().topLeft().button("All");
        button.shouldExists();
        button.shouldHaveColor(Colors.PRIMARY);
        button.hover();
        tooltip = button.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText(new String[]{"value1"});
    }

    @Test
    public void testRounded() {
        ListWidget list = page.widget(ListWidget.class);
        StandardButton button = list.toolbar().topLeft().button("All");
        button.shouldExists();
        button.shouldNotBeRounded();
        button = list.toolbar().topLeft().button("rounded");
        button.shouldExists();
        button.shouldBeRounded();
    }
}