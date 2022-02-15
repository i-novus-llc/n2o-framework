package net.n2oapp.framework.autotest.fieldset;

import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.fieldset.LineFieldSet;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для подсказок филдсетов и полей
 */
public class HelpMessageAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oFieldSetsPack(), new N2oControlsPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/fieldset/help/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/fieldset/help/test.query.xml"));
    }

    @Test
    public void testHelpMessage() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Филдсеты и поля с подсказками");

        SimpleFieldSet simpleFieldSet = page.widget(FormWidget.class).fieldsets().fieldset(SimpleFieldSet.class);
        simpleFieldSet.shouldHaveHelp();
        simpleFieldSet.clickHelp();
        page.popover("Этот филдсет содержит поля: Серия и Номер").shouldBeVisible();

        StandardField field = simpleFieldSet.fields().field("Серия");
        field.shouldExists();
        field.shouldHaveHelp();
        field.clickHelp();
        page.popover("Серия паспорта").shouldBeVisible();

        field = simpleFieldSet.fields().field("Номер");
        field.shouldExists();
        field.shouldHaveHelp();
        field.clickHelp();
        page.popover("Номер паспорта").shouldBeVisible();

        LineFieldSet lineFieldSet = page.widget(FormWidget.class).fieldsets().fieldset(LineFieldSet.class);
        lineFieldSet.shouldHaveHelp();
        lineFieldSet.clickHelp();
        page.popover("Этот филдсет содержит поля: Выпадающий список и Множественный выбор").shouldBeVisible();

        MultiFieldSet multiFieldSet = page.widget(FormWidget.class).fieldsets().fieldset(2, MultiFieldSet.class);
        multiFieldSet.shouldHaveHelp();
        multiFieldSet.clickHelp();
        page.popover("Этот филдсет содержит поля: Имя и Возраст").shouldBeVisible();
    }
}
