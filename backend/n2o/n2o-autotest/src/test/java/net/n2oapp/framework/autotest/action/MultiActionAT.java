package net.n2oapp.framework.autotest.action;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
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
 * Тестирование мультидействия
 */
public class MultiActionAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
    }


    @Test
    public void testMulti() {
        setJsonPath("net/n2oapp/framework/autotest/action/multi");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/multi/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi/page.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        Button button = table.toolbar().topLeft().button("Открыть");

        InputText name = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("name").control(InputText.class);

        table.columns().rows().row(1).click();
        button.click();
        name.shouldHaveValue("test2");
        Selenide.back();

        table.columns().rows().row(3).click();
        button.click();
        name.shouldHaveValue("test4");
        Selenide.back();
    }

    @Test
    public void testActionAfterFail() {
        setJsonPath("net/n2oapp/framework/autotest/action/multi/action_after_fail");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/multi/action_after_fail/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi/action_after_fail/test.object.xml"));

        StandardPage page = open(StandardPage.class);
        InputText inputText = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).fields().field("input").control(InputText.class);
        Button button = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).toolbar().topLeft().button("Кнопка");

        inputText.setValue("test");
        button.click();
        page.alerts(Alert.Placement.top).alert(0).shouldExists();
        inputText.shouldHaveValue("test");

        inputText.setValue("123");
        button.click();
        inputText.shouldBeEmpty();
    }
}
