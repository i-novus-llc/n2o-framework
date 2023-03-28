package net.n2oapp.framework.autotest.submodels;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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
 * Проверка разрешения сабмоделей в полях формы
 */
public class ResolveFormFieldSubModelsAT extends AutoTestBase {

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
    public void resolveTableFiltersSubModels() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/submodels/form_fields/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/submodels/form_fields/new_page.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/submodels/form_fields/type.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FormWidget form = page.widget(FormWidget.class);
        form.shouldExists();
        StandardButton openBtn = form.toolbar().topLeft().button("Open");
        openBtn.shouldExists();
        openBtn.click();

        SimplePage newPage = N2oSelenide.page(SimplePage.class);
        newPage.shouldExists();
        Fields fields = newPage.widget(FormWidget.class).fields();
        InputSelect genderField = fields.field("Gender").control(InputSelect.class);
        genderField.shouldExists();
        genderField.shouldSelected("Men");
        InputSelect typeField = fields.field("Type").control(InputSelect.class);
        typeField.shouldExists();
        typeField.shouldSelected("type2");
    }

    @Test
    public void resolveTwoFormFiltersSubModels() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/submodels/one_more_form_fields/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/submodels/one_more_form_fields/test.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/submodels/one_more_form_fields/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/submodels/one_more_form_fields/gender.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        TableWidget table = simplePage.widget(TableWidget.class);
        Cells row = table.columns().rows().row(2);
        row.click();

        table.toolbar().topLeft().button("Изменить").click();

        StandardPage page = N2oSelenide.page(StandardPage.class);
        page.shouldExists();

        SimpleRegion region = page.regions().region(0, SimpleRegion.class);
        FormWidget firstForm = region.content().widget(0, FormWidget.class);
        FormWidget secondForm = region.content().widget(1, FormWidget.class);

        firstForm.fields().field("gender").control(Select.class).shouldSelected("female");
        secondForm.fields().field("gender").control(Select.class).shouldSelected("female");
    }
}
