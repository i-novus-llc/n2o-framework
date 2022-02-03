package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
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
 * Автотест на чтение значения поля из модели текущей страницы и родительской
 */
public class RefFieldModelAT extends AutoTestBase {
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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/model/field/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/model/field/page.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/model/field/test.query.xml"));
    }

    @Test
    public void refFieldModelTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        table.columns().rows().row(1).click();
        table.toolbar().topLeft().button("Модальное окно").click();

        // modal
        Modal modal = N2oSelenide.modal();
        StandardPage openPage = modal.content(StandardPage.class);
        RegionItems widgets = openPage.regions().region(0, SimpleRegion.class).content();
        InputText id = widgets.widget(0, FormWidget.class).fields().field("id").control(InputText.class);
        id.shouldHaveValue("2");

        Fields fields = widgets.widget(1, FormWidget.class).fields();
        InputText fromCurrentPageWidget = fields.field("Чтение из виджета текущей страницы").control(InputText.class);
        fromCurrentPageWidget.shouldHaveValue("2");
        InputText fromParentPageWidget = fields.field("Чтение из виджета родительской страницы").control(InputText.class);
        fromParentPageWidget.shouldHaveValue("2");
        modal.close();

        // open-page
        table.columns().rows().row(2).click();
        table.toolbar().topLeft().button("Открыть").click();
        openPage = N2oSelenide.page(StandardPage.class);
        widgets = openPage.regions().region(0, SimpleRegion.class).content();
        id = widgets.widget(0, FormWidget.class).fields().field("id").control(InputText.class);
        id.shouldHaveValue("3");

        fields = widgets.widget(1, FormWidget.class).fields();
        fromCurrentPageWidget = fields.field("Чтение из виджета текущей страницы").control(InputText.class);
        fromCurrentPageWidget.shouldHaveValue("3");
        fromParentPageWidget = fields.field("Чтение из виджета родительской страницы").control(InputText.class);
        fromParentPageWidget.shouldHaveValue("3");
    }
}
