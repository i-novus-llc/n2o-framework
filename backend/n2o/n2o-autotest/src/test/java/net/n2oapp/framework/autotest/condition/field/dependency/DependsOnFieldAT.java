package net.n2oapp.framework.autotest.condition.field.dependency;

import com.codeborne.selenide.Configuration;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.drawer.Drawer;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
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
        builder.packs(
                new N2oAllPagesPack(),
                new N2oAllDataPack(),
                new N2oApplicationPack()
        );
    }

    @Test
    public void onField() {
        setJsonPath("net/n2oapp/framework/autotest/condition/field/dependency/on_field");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/on_field/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/on_field/orgs.query.xml")
        );
        
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        Fields fields = page.widget(FormWidget.class).fields();

        InputText master = fields.field(    "Управляющее поле").control(InputText.class);
        master.shouldExists();
        master.shouldBeEmpty();

        StandardField dependent = fields.field("Зависимое поле");
        dependent.shouldNotExists();

        master.click();
        master.setValue("test");
        master.shouldHaveValue("test");

        dependent.shouldExists();
        dependent.shouldBeRequired();
        dependent.control(InputText.class).shouldBeEnabled();
        dependent.control(InputText.class).shouldHaveValue("test");

        master.click();
        master.setValue("1");
        master.shouldHaveValue("1");
        StandardField dependentList = fields.field("Зависимый список");
        dependentList.shouldExists();
        dependentList.control(InputSelect.class).shouldSelectedMulti("orgs1", "orgs2");
    }

    @Test
    public void onDifferentNameField() {
        setJsonPath("net/n2oapp/framework/autotest/condition/field/dependency/on_different_name_field");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/on_different_name_field/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/on_different_name_field/data.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/on_different_name_field/modal.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Зависимость от поля с другим именем в дровере");

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
        drawerField.openPopup();
        drawerField.dropdown().selectItem(0);

        drawer.toolbar().bottomRight().button("Применить").click();
        table.columns().rows().shouldHaveSize(1);
        tableField.shouldHaveValue("region1");
    }
    
    @Test
    void checkDependencyBetweenFieldsAndMultifieldsets() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/on_fields_and_multifieldsets/index.page.xml")
        );
        final StandardPage page = open(StandardPage.class);
        page.shouldExists();
        
        final FormWidget formWidget = page.regions()
                .region(0, SimpleRegion.class)
                .content()
                .widget(1, FormWidget.class);

        final Fields buttonFields = formWidget.fieldsets().fieldset(0, SimpleFieldSet.class).fields();
        final ButtonField resetFieldBtn = buttonFields.field("reset field", ButtonField.class);
        final ButtonField resetFieldsetBtn = buttonFields.field("reset field.multiSet", ButtonField.class);
        final ButtonField setValueToFieldsetBtn = buttonFields.field("set default to field..multiSet", ButtonField.class);
        final MultiFieldSet fieldset = formWidget.fieldsets().fieldset(1, MultiFieldSet.class);
        final InputText input = buttonFields.field("field.input").control(InputText.class);
        final Fields outputFields = formWidget.fieldsets().fieldset(2, SimpleFieldSet.class).fields();
        final OutputText onAnyField = outputFields.field("on=field").control(OutputText.class);
        final OutputText onFieldset = outputFields.field("on=field.multiSet").control(OutputText.class);
        final OutputText onInput = outputFields.field("on field.input").control(OutputText.class);

        resetFieldBtn.shouldExists();
        resetFieldsetBtn.shouldExists();
        setValueToFieldsetBtn.shouldExists();
        fieldset.shouldExists();
        input.shouldBeEmpty();
        onAnyField.shouldBeEmpty();
        onFieldset.shouldBeEmpty();
        onInput.shouldBeEmpty();
        
        input.setValue("1");

        //was changed
        onAnyField.shouldHaveValue("3");
        onInput.shouldHaveValue("3");
        //wasn't changed
        onFieldset.shouldBeEmpty();
        
        setValueToFieldsetBtn.click();

        Fields fieldsetFields = fieldset.item(0).fields();
        final Select select = fieldsetFields.field("field.multiSet[0].select").control(Select.class);
        final InputText inputInFieldset = fieldsetFields.field("field.multiSet[0].input").control(InputText.class);
        final OutputText onAnyFieldInFieldset = fieldsetFields.field("on=field.multiSet[index]").control(OutputText.class);
        final OutputText onSelect = fieldsetFields.field("on=field.multiSet[index].select").control(OutputText.class);

        select.shouldSelected("second");
        //was changed
        onAnyField.shouldHaveValue("4");
        onFieldset.shouldHaveValue("4");
        onAnyFieldInFieldset.shouldHaveValue("4");
        onSelect.shouldHaveValue("4");
        //wasn't changed
        /*ToDo: Раскомментить после закрытия задачи NNO-9508
            onInput.shouldHaveValue("1");
         */
        
        inputInFieldset.setValue("2");

        //was changed
        onAnyField.shouldHaveValue("5");
        onFieldset.shouldHaveValue("5");
        onAnyFieldInFieldset.shouldHaveValue("5");
        //wasn't changed
        onSelect.shouldHaveValue("4");
        /*ToDo: Раскомментить после закрытия задачи NNO-9508
            onInput.shouldHaveValue("1");
         */

        resetFieldsetBtn.click();

        //was changed
        onAnyField.shouldHaveValue("6");
        onFieldset.shouldHaveValue("6");
        //wasn't changed
        /*ToDo: Раскомментить после закрытия задачи NNO-9508
            onInput.shouldHaveValue("1");
         */
        
        resetFieldBtn.click();

        //was changed
        onAnyField.shouldHaveValue("7");
        onFieldset.shouldHaveValue("7");
        onInput.shouldHaveValue("7");
    }
}
