package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
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
 * Автотест проверяет работу clear
 */
public class ClearActionAT extends AutoTestBase {

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
    public void testClearInModal() {
        setJsonPath("net/n2oapp/framework/autotest/action/clear");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/clear/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/clear/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/clear/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions()
                .region(0, SimpleRegion.class)
                .content()
                .widget(TableWidget.class);
        table.shouldExists();

        StandardButton openBtn = table.toolbar().topLeft().button("Открыть");
        openBtn.shouldExists();

        table.columns().rows().row(2).click();
        openBtn.click();

        Modal modal = N2oSelenide.modal();
        modal.shouldExists();

        StandardPage modalPage = modal.content(StandardPage.class);
        FormWidget form = modalPage.regions()
                .region(0, SimpleRegion.class)
                .content()
                .widget(FormWidget.class);

        InputText id = form.fields().field("id").control(InputText.class);
        InputText name = form.fields().field("name").control(InputText.class);
        id.shouldHaveValue("3");
        name.shouldHaveValue("test3");

        form.toolbar().topLeft().button("Очистить поля").click();
        id.shouldBeEmpty();
        name.shouldBeEmpty();

        modal.close();
    }

    @Test
    public void clearAfterAction() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/clear/clearAfterAction/index.page.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget form = page.regions()
                .region(0, SimpleRegion.class)
                .content()
                .widget(FormWidget.class);
        form.shouldExists();

        InputText inputText = form.fields().field("Исходные данные").control(InputText.class);
        inputText.shouldHaveValue("Привет, Мир!");

        StandardButton clearBtn = form.toolbar().topLeft().button("clear");
        StandardButton copyBtn = form.toolbar().topLeft().button("copy");

        clearBtn.click();
        inputText.shouldBeEmpty();

        copyBtn.click();
        inputText.shouldHaveValue("value");

        clearBtn.click();
        inputText.shouldBeEmpty();
    }
}
