package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
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
        builder.packs(new N2oAllPagesPack(), new N2oHeaderPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/copy/test.query.xml"));
    }

    @Test
    public void testSimpleCopy() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/copy/simple/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.widgets().widget(TableWidget.class);
        FormWidget form = page.widgets().widget(1, FormWidget.class);
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

        FormWidget form = page.single().widget(FormWidget.class);

        InputText id = form.fields().field("id").control(InputText.class);
        id.shouldBeEmpty();
        InputText name = form.fields().field("name").control(InputText.class);
        name.shouldBeEmpty();

        StandardButton btn = form.toolbar().topLeft().button("Открыть");

        // копирование второй строки
        btn.click();
        Modal modal = N2oSelenide.modal();
        TableWidget table = modal.content(SimplePage.class).single().widget(TableWidget.class);
        StandardButton saveBtn = modal.toolbar().bottomRight().button("Выбрать");

        table.columns().rows().row(1).click();
        saveBtn.click();
        modal.shouldNotExists();

        id.shouldHaveValue("2");
        name.shouldHaveValue("test2");

        // копирование четвертой строки
        btn.click();
        modal = N2oSelenide.modal();
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

        FormWidget form = page.single().widget(FormWidget.class);
        MultiFieldSet multiSet = form.fieldsets().fieldset(MultiFieldSet.class);

        StandardButton btn = form.toolbar().topLeft().button("Выбрать");

        // копирование первой и третьей строки
        btn.click();
        Modal modal = N2oSelenide.modal();
        TableWidget table = modal.content(SimplePage.class).single().widget(TableWidget.class);
        StandardButton saveBtn = modal.toolbar().bottomRight().button("Выбрать");

        table.columns().rows().row(0).click();
        table.columns().rows().row(2).click();
        saveBtn.click();
        modal.shouldNotExists();


    }
}
