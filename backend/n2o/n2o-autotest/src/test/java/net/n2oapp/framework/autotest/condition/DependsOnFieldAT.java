package net.n2oapp.framework.autotest.condition;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.drawer.Drawer;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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
 * Тест зависимостей от поля
 */
public class DependsOnFieldAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack(), new N2oApplicationPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    @Test
    public void dependsOnField() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/condition/depends_on/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        Fields fields = page.widget(FormWidget.class).fields();

        InputText master = fields.field("Управляющее поле").control(InputText.class);
        master.shouldExists();
        master.shouldBeEmpty();

        StandardField dependent = fields.field("Зависимое поле");
        dependent.shouldNotExists();

        master.val("test");
        master.shouldHaveValue("test");

        dependent.shouldExists();
        dependent.shouldBeRequired();
        dependent.control(InputText.class).shouldBeEnabled();
        dependent.control(InputText.class).shouldHaveValue("test");
    }

    @Test
    public void dependsOnFieldWithDifferentNameInDrawer() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/depends_on/field_with_different_name/drawer/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/depends_on/field_with_different_name/drawer/data.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/depends_on/field_with_different_name/drawer/modal.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Зависимость от поля с другим именем в дровере");

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().rows().shouldHaveSize(2);

        StandardButton button = table.toolbar().topLeft().button("Фильтр");
        button.shouldExists();
        button.shouldBeEnabled();

        InputText tableField = table.filters().fields().field("Значение из поля дровера").control(InputText.class);
        tableField.shouldBeDisabled();

        button.click();

        Drawer drawer = N2oSelenide.drawer();
        SimplePage drawerPage =  drawer.content(SimplePage.class);
        drawerPage.shouldExists();

        InputSelect drawerField = drawerPage.widget(FormWidget.class).fields().field("Регион").control(InputSelect.class);
        drawerField.shouldExists();
        drawerField.shouldBeEmpty();
        drawerField.select(0);

        drawer.toolbar().bottomRight().button("Применить").click();
        table.columns().rows().shouldHaveSize(1);
        tableField.shouldHaveValue("region1");
    }
}
