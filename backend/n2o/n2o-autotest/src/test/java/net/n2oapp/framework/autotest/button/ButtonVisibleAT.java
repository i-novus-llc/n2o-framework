package net.n2oapp.framework.autotest.button;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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
 * Автотест для условия видимости кнопок
 */
public class ButtonVisibleAT extends AutoTestBase {

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
    }

    @Test
    public void testVisibleButton() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/button/visible/simple/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        RadioGroup type = form.fields().field("type").control(RadioGroup.class);
        Toolbar toolbar = form.toolbar().bottomLeft();
        StandardButton hiddenButton = toolbar.button("Hidden button");
        StandardButton button = toolbar.button("Button");
        DropdownButton subMenu = toolbar.dropdown();
        StandardButton item1 = subMenu.menuItem("item1");
        StandardButton item2 = subMenu.menuItem("item2");

        type.shouldBeEmpty();
        hiddenButton.shouldNotExists();
        button.shouldNotExists();
        subMenu.shouldExists();
        subMenu.shouldBeHidden();

        type.check("button and all menu items");
        button.shouldExists();
        subMenu.shouldExists();
        subMenu.click();
        subMenu.shouldHaveItems(2);
        item1.shouldExists();
        item2.shouldExists();

        type.check("first menu item");
        button.shouldNotExists();
        subMenu.click();
        subMenu.shouldHaveItems(1);
        item1.shouldExists();

        type.check("second menu item");
        button.shouldNotExists();
        subMenu.click();
        subMenu.shouldHaveItems(1);
        item2.shouldExists();

        type.check("none");
        button.shouldNotExists();
        subMenu.shouldExists();
        subMenu.shouldBeHidden();
    }

    @Test
    public void testVisibleSubMenu() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/button/visible/submenu/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        RadioGroup type = form.fields().field("type").control(RadioGroup.class);
        Toolbar toolbar = form.toolbar().bottomLeft();
        DropdownButton hiddenSubMenu = toolbar.dropdown(Condition.text("Hidden submenu"));
        DropdownButton subMenu = toolbar.dropdown(Condition.textCaseSensitive("SubMenu"));
        StandardButton item1 = subMenu.menuItem("item1");
        StandardButton item2 = subMenu.menuItem("item2");

        type.shouldBeEmpty();
        hiddenSubMenu.shouldNotExists();
        subMenu.shouldNotExists();

        type.check("show");
        subMenu.shouldExists();
        subMenu.click();
        subMenu.shouldHaveItems(2);
        item1.shouldExists();
        item2.shouldExists();

        type.check("hide");
        subMenu.shouldNotExists();
        hiddenSubMenu.shouldNotExists();
    }
}
