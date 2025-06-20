package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.CheckboxCell;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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
 * Автотест вызова операции
 */
class InvokeActionAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
    }

    @Test
    void testFormParam() {
        setResourcePath("net/n2oapp/framework/autotest/action/invoke/form_param");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/invoke/form_param/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/invoke/form_param/test.object.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget firstForm = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);
        InputText firstValue = firstForm.fields().field("value").control(InputText.class);
        firstValue.click();
        firstValue.setValue("1");
        FormWidget secondForm = page.regions().region(1, SimpleRegion.class).content().widget(0, FormWidget.class);
        InputText secondValue = secondForm.fields().field("value").control(InputText.class);
        InputText secondValue2 = secondForm.fields().field("value2").control(InputText.class);
        secondValue.click();
        secondValue.setValue("2");
        secondValue2.click();
        secondValue2.setValue("3");

        StandardButton btn = page.toolbar().bottomRight().button("Сохранить");
        btn.click();
        Alert alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldHaveColor(ColorsEnum.SUCCESS);
        alert.shouldHaveText("form_value=1 second_form.value=2 second_form.value2=3");

        FormWidget thirdForm = page.regions().region(2, SimpleRegion.class).content().widget(0, FormWidget.class);
        InputText input = thirdForm.fields().field("name").control(InputText.class);
        input.click();
        input.setValue("text");

        ButtonField button = thirdForm.fields().field("Сохранить (submit-all='false')", ButtonField.class);
        button.click();
        OutputText result = thirdForm.fields().field("result").control(OutputText.class);
        result.shouldHaveValue("SUCCESS");
        OutputText resultName = thirdForm.fields().field("resultName").control(OutputText.class);
        resultName.shouldHaveValue("text");
    }

    @Test
    void testSubmitAll() {
        setResourcePath("net/n2oapp/framework/autotest/action/invoke/submit_all");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/invoke/submit_all/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/invoke/submit_all/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/invoke/submit_all/test.query.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        TableWidget.Rows rows = table.columns().rows();

        rows.shouldHaveSize(4);
        rows.row(1).cell(0, CheckboxCell.class).setChecked(true);
        rows.row(2).cell(0, CheckboxCell.class).setChecked(true);

        table.toolbar().topLeft().button("Отправить (submit-all='true')").click();
        Alert alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldHaveColor(ColorsEnum.SUCCESS);
        alert.shouldHaveText("[{id=2, name=test2, ids=[2, 3]}, {id=3, name=test3, ids=[2, 3]}]");

        table.toolbar().topLeft().button("Отправить (submit-all='false')").click();
        alert = page.alerts(Alert.PlacementEnum.TOP).alert(1);
        alert.shouldHaveColor(ColorsEnum.SUCCESS);
        alert.shouldHaveText("[2, 3]");
    }
}
