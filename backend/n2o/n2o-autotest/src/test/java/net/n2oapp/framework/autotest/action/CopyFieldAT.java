package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CopyFieldAT extends AutoTestBase {

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
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/copy/field/index.page.xml"));
    }

    @Test
    void testCopyField() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Копирование значения поля");
        Button button = page.widget(FormWidget.class).toolbar().topLeft().button("Копировать");
        button.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();

        InputText field1 = fields.field("Копируемое значение").control(InputText.class);
        field1.shouldExists();
        field1.shouldBeEmpty();

        InputText field2 = fields.field("Скопированное значение").control(InputText.class);
        field2.shouldExists();
        field2.shouldBeEmpty();

        field1.click();
        field1.setValue("test1");
        button.click();
        field2.shouldHaveValue("test1");

        field1.click();
        field1.setValue("0");
        button.click();
        field2.shouldHaveValue("0");
    }
}