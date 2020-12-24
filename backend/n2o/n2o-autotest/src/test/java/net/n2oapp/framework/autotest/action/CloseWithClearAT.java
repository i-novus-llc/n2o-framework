package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.drawer.Drawer;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест очистки формы с закрытием
 */
public class CloseWithClearAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oHeaderPack());
    }

    @Test
    public void testCloseWithClearModal() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/close_with_clear/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/close_with_clear/modal.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.breadcrumb().titleShouldHaveText("close-with-clear-test");
        page.shouldExists();

        page.widget(FormWidget.class).toolbar().topLeft().button("openModal").shouldBeEnabled();
        page.widget(FormWidget.class).toolbar().topLeft().button("openModal").click();
        Modal modal = N2oSelenide.modal();
        modal.shouldExists();
        modal.shouldHaveTitle("TestPage");
        StandardField valueField = modal.content(SimplePage.class).widget(FormWidget.class).fields().field("value");
        valueField.shouldExists();
        InputText inputText = valueField.control(InputText.class);
        inputText.clear();
        inputText.val("testValue1");
        inputText.shouldHaveValue("testValue1");

        Toolbar toolbar = modal.content(SimplePage.class).widget(FormWidget.class).toolbar().topLeft();
        toolbar.button("Clear").shouldBeEnabled();
        toolbar.button("Clear").click();
        modal.shouldExists();
        modal.shouldHaveTitle("TestPage");
        inputText.shouldBeEmpty();

        inputText.val("testValue2");
        inputText.shouldHaveValue("testValue2");

        toolbar.button("CloseWithClear").shouldBeEnabled();
        toolbar.button("CloseWithClear").click();
        modal.shouldNotExists();

        page.widget(FormWidget.class).toolbar().topLeft().button("openModal").click();
        modal.shouldExists();
        inputText.shouldBeEmpty();
        modal.close();
        modal.shouldNotExists();
    }

    @Test
    public void testCloseWithClearDrawer() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/close_with_clear/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/close_with_clear/modal.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.breadcrumb().titleShouldHaveText("close-with-clear-test");
        page.shouldExists();

        page.widget(FormWidget.class).toolbar().topLeft().button("openDrawer").shouldBeEnabled();
        page.widget(FormWidget.class).toolbar().topLeft().button("openDrawer").click();

        Drawer drawer = N2oSelenide.drawer();
        drawer.shouldExists();
        drawer.shouldHaveTitle("TestPage");
        StandardField valueField = drawer.content(SimplePage.class).widget(FormWidget.class).fields().field("value");
        valueField.shouldExists();
        InputText inputText = valueField.control(InputText.class);
        inputText.clear();
        inputText.val("testValue1");
        inputText.shouldHaveValue("testValue1");

        Toolbar toolbar = drawer.content(SimplePage.class).widget(FormWidget.class).toolbar().topLeft();
        toolbar.button("Clear").shouldBeEnabled();
        toolbar.button("Clear").click();
        drawer.shouldExists();
        drawer.shouldHaveTitle("TestPage");
        inputText.shouldBeEmpty();

        inputText.val("testValue2");
        inputText.shouldHaveValue("testValue2");

        toolbar.button("CloseWithClear").shouldBeEnabled();
        toolbar.button("CloseWithClear").click();
        drawer.shouldNotExists();

        page.widget(FormWidget.class).toolbar().topLeft().button("openDrawer").click();
        drawer.shouldExists();
        inputText.shouldBeEmpty();
        drawer.close();
        drawer.shouldNotExists();
    }

}
