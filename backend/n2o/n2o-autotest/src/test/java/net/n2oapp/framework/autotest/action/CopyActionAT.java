package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSetItem;
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
 * Автотест для действия копирования
 */
public class CopyActionAT extends AutoTestBase {

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

        setJsonPath("net/n2oapp/framework/autotest/action/copy");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/copy/test.query.xml"));
    }

    @Test
    public void testSimpleCopy() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/copy/simple/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RegionItems content = page.regions().region(0, SimpleRegion.class).content();
        TableWidget table = content.widget(TableWidget.class);
        FormWidget form = content.widget(1, FormWidget.class);
        InputText id = form.fields().field("id").control(InputText.class);
        InputText name = form.fields().field("name").control(InputText.class);
        StandardButton copyBtn = table.toolbar().bottomRight().button("Копирование");

        // копирование второй строки
        table.columns().rows().row(1).click();
        copyBtn.click();
        id.shouldHaveValue("2");
        name.shouldHaveValue("test2");

        // копирование четвертой строки
        table.columns().rows().row(3).click();
        copyBtn.click();
        id.shouldHaveValue("4");
        name.shouldHaveValue("test4");
    }

    @Test
    public void testCopyFromModal() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/copy/modal/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/copy/modal/modal.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);

        InputText id = form.fields().field("id").control(InputText.class);
        id.shouldBeEmpty();
        InputText name = form.fields().field("name").control(InputText.class);
        name.shouldBeEmpty();

        StandardButton btn = form.toolbar().topLeft().button("Открыть");
        btn.click();

        // копирование второй строки

        Modal modal = N2oSelenide.modal();
        modal.shouldExists();

        SimplePage modalPage = modal.content(SimplePage.class);
        modalPage.shouldExists();

        TableWidget table = modalPage.widget(TableWidget.class);
        table.shouldExists();

        StandardButton saveBtn = modal.toolbar().bottomRight().button("Сохранить");

        table.columns().rows().shouldHaveSize(4);
        table.columns().rows().row(1).click();
        saveBtn.click();
        modal.shouldNotExists();

        form.shouldExists();
        id.shouldExists();
        id.shouldHaveValue("2");
        name.shouldHaveValue("test2");

        // копирование четвертой строки
        btn.click();
        modal = N2oSelenide.modal();
        modal.shouldBeVisible();
        table.shouldBeVisible();
        table.columns().rows().row(3).click();
        saveBtn.click();
        modal.shouldNotExists();

        id.shouldHaveValue("4");
        name.shouldHaveValue("test4");
    }

    @Test
    public void testMultiCopyFromModal() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/copy/modal_multi/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/copy/modal_multi/modal.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        MultiFieldSet multiSet = form.fieldsets().fieldset(MultiFieldSet.class);

        StandardButton btn = form.toolbar().topLeft().button("Выбрать");

        // копирование первой и третьей строки
        // COPY-MODE=ADD
        btn.click();
        Modal modal = N2oSelenide.modal();
        TableWidget table = modal.content(SimplePage.class).widget(TableWidget.class);
        StandardButton saveBtn = modal.toolbar().bottomRight().button("Сохранить");

        table.columns().rows().row(0).click();
        table.columns().rows().row(2).click();
        saveBtn.click();
        modal.shouldNotExists();

        // проверяем наличие двух элементов в мультисете
        multiSet.shouldHaveItems(2);
        MultiFieldSetItem item1 = multiSet.item(0);
        MultiFieldSetItem item2 = multiSet.item(1);
        item1.fields().field("id").control(InputText.class).shouldHaveValue("1");
        item1.fields().field("name").control(InputText.class).shouldHaveValue("test1");
        item2.fields().field("id").control(InputText.class).shouldHaveValue("3");
        item2.fields().field("name").control(InputText.class).shouldHaveValue("test3");

        // копирование первой и четвертой строки
        btn.click();
        modal = N2oSelenide.modal();

        table.columns().rows().row(3).click();
        table.columns().rows().row(0).click();
        saveBtn.click();
        modal.shouldNotExists();

        // проверяем наличие четырех элементов в мультисете
        // первая строка из таблицы должна присутствовать дважды
        multiSet.shouldHaveItems(4);
        MultiFieldSetItem item3 = multiSet.item(2);
        MultiFieldSetItem item4 = multiSet.item(3);
        item1.fields().field("id").control(InputText.class).shouldHaveValue("1");
        item1.fields().field("name").control(InputText.class).shouldHaveValue("test1");
        item2.fields().field("id").control(InputText.class).shouldHaveValue("3");
        item2.fields().field("name").control(InputText.class).shouldHaveValue("test3");
        item3.fields().field("id").control(InputText.class).shouldHaveValue("1");
        item3.fields().field("name").control(InputText.class).shouldHaveValue("test1");
        item4.fields().field("id").control(InputText.class).shouldHaveValue("4");
        item4.fields().field("name").control(InputText.class).shouldHaveValue("test4");
    }

    @Test
    public void testReplaceCopyFromModal() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/copy/modal_replace/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/copy/modal_replace/modal.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        StandardField field = page.widget(FormWidget.class).fields().field("Адрес");
        InputText address = field.control(InputText.class);
        address.shouldBeEmpty();
        StandardButton btn = field.toolbar().button("Ввести новый");

        // копирование значений нескольких полей
        // COPY-MODE=REPLACE
        btn.click();
        Modal modal = N2oSelenide.modal();
        FormWidget modalForm = modal.content(SimplePage.class).widget(FormWidget.class);
        InputText city = modalForm.fields().field("Город").control(InputText.class);
        InputText street = modalForm.fields().field("Улица").control(InputText.class);
        city.click();
        city.setValue("NY");
        street.click();
        street.setValue("Wall Street");
        StandardButton saveBtn = modal.toolbar().bottomRight().button("Сохранить");
        saveBtn.click();

        // проверяем, что оба значения скопировались
        address.shouldHaveValue("NY, Wall Street");

        // копируем только улицу
        btn.click();

        city.shouldBeEmpty();
        street.click();
        street.setValue("Broadway");
        saveBtn.click();

        address.shouldHaveValue("Broadway");
    }

    /**
     * Тест, проверяющий copy-mode=add c вложенными полями
     */
    @Test
    public void testCopyNestedFields() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/copy/nested_fields/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/copy/nested_fields/head_add.page.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.shouldExists();

        StandardButton addBtn = table.toolbar().topLeft().button("Добавить");

        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(0);

        addBtn.click();

        Modal modal = N2oSelenide.modal();
        modal.shouldExists();

        FormWidget form = modal.content(StandardPage.class)
                .regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        form.shouldExists();

        InputText inputText = form.fields().field("rpu.name").control(InputText.class);
        inputText.click();
        inputText.setValue("val1");

        StandardButton saveBtn = modal.toolbar().bottomRight().button("Сохранить");
        saveBtn.shouldExists();
        saveBtn.click();

        rows.shouldHaveSize(1);
        rows.row(0).cell(1).shouldHaveText("val1");

        addBtn.click();
        modal.shouldExists();
        inputText.click();
        inputText.setValue("val2");
        saveBtn.click();
        rows.shouldHaveSize(2);
        rows.row(1).cell(1).shouldHaveText("val2");
    }
}
