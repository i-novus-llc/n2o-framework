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
class HelpMessageAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oFieldSetsPack(), new N2oControlsPack(), new N2oAllDataPack());
    }

    @Test
    void testHelpMessageClick() {
        setResourcePath("net/n2oapp/framework/autotest/fieldset/help/click");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/fieldset/help/click/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/fieldset/help/click/test.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Филдсеты и поля с подсказками по клику на иконку с вопросом");

        SimpleFieldSet simpleFieldSet = page.widget(FormWidget.class).fieldsets().fieldset(0, SimpleFieldSet.class);
        simpleFieldSet.shouldHaveHelp();
        simpleFieldSet.clickHelp();
        page.popover("Этот филдсет содержит поля: Серия и Номер").shouldBeVisible();

        StandardField field = simpleFieldSet.fields().field("Серия");
        field.shouldExists();
        field.shouldHaveHelp();
        field.clickHelp();
        page.popover("Серия паспорта").shouldBeVisible();
        page.popover("Этот филдсет содержит поля: Серия и Номер").shouldBeClosed();

        field = simpleFieldSet.fields().field("Номер");
        field.shouldExists();
        field.shouldHaveHelp();
        field.clickHelp();
        page.popover("Номер паспорта").shouldBeVisible();

        LineFieldSet lineFieldSet = page.widget(FormWidget.class).fieldsets().fieldset(1, LineFieldSet.class);
        lineFieldSet.shouldHaveHelp();
        lineFieldSet.clickHelp();
        page.popover("Этот филдсет содержит поля: Выпадающий список и Множественный выбор").shouldBeVisible();
        page.popover("Номер паспорта").shouldBeClosed();

        MultiFieldSet multiFieldSet = page.widget(FormWidget.class).fieldsets().fieldset(2, MultiFieldSet.class);
        multiFieldSet.shouldHaveHelp();
        multiFieldSet.clickHelp();
        page.popover("Этот филдсет содержит поля: Имя и Возраст").shouldBeVisible();
        page.popover("Этот филдсет содержит поля: Выпадающий список и Множественный выбор").shouldBeClosed();
    }

    @Test
    void testHelpMessageHover() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/fieldset/help/hover/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Филдсеты и поля с подсказками при наведении курсора на иконку с вопросом");

        SimpleFieldSet simpleFieldSet = page.widget(FormWidget.class).fieldsets().fieldset(0, SimpleFieldSet.class);
        simpleFieldSet.shouldHaveHelp();
        simpleFieldSet.hoverHelp();
        page.popover("Подсказка1").shouldBeVisible();

        StandardField field = simpleFieldSet.fields().field("InputText");
        field.shouldExists();
        field.shouldHaveHelp();
        field.hoverHelp();
        page.popover("Подсказка2").shouldBeVisible();
        page.popover("Подсказка1").shouldBeClosed();

        field = simpleFieldSet.fields().field("DateTime");
        field.shouldExists();
        field.shouldHaveHelp();
        field.hoverHelp();
        page.popover("Подсказка3").shouldBeVisible();

        field = simpleFieldSet.fields().field("Checkbox");
        field.shouldExists();
        field.shouldHaveHelp();
        field.hoverHelp();
        page.popover("Подсказка4").shouldBeVisible();

        LineFieldSet lineFieldSet = page.widget(FormWidget.class).fieldsets().fieldset(1, LineFieldSet.class);
        lineFieldSet.shouldHaveHelp();
        lineFieldSet.hoverHelp();
        page.popover("Подсказка5").shouldBeVisible();
        page.popover("Подсказка4").shouldBeClosed();

        field = lineFieldSet.fields().field("InputSelect");
        field.shouldExists();
        field.shouldHaveHelp();
        field.hoverHelp();
        page.popover("Подсказка6").shouldBeVisible();

        field = lineFieldSet.fields().field("DateInterval");
        field.shouldExists();
        field.shouldHaveHelp();
        field.hoverHelp();
        page.popover("Подсказка7").shouldBeVisible();

        MultiFieldSet multiFieldSet = page.widget(FormWidget.class).fieldsets().fieldset(2, MultiFieldSet.class);
        multiFieldSet.shouldHaveHelp();
        multiFieldSet.hoverHelp();
        page.popover("Подсказка8").shouldBeVisible();
        page.popover("Подсказка7").shouldBeClosed();
    }
}
