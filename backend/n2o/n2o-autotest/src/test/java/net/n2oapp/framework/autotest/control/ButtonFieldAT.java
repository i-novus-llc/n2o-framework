package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.Checkbox;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
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
 * Автотест для компонента поля button
 */
class ButtonFieldAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    void testSimple() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/button_field/simple/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        Fields fields = page.widget(FormWidget.class).fields();

        ButtonField buttonField = page.widget(FormWidget.class).fields().field("Кнопка-поле закругленная", ButtonField.class);
        buttonField.shouldExists();
        buttonField.shouldBeRounded();
        buttonField.shouldHaveIcon("fa fa-search");
        buttonField.shouldHaveColor(ColorsEnum.PRIMARY);
        buttonField.hover();
        buttonField.tooltip().shouldExists();
        buttonField.tooltip().shouldHaveText(new String[]{"description"});
        buttonField.tooltipShouldHavePosition("top");

        buttonField = page.widget(FormWidget.class).fields().field("Кнопка-поле1", ButtonField.class);
        buttonField.shouldExists();
        buttonField.shouldNotBeRounded();
        buttonField.shouldHaveColor(ColorsEnum.SUCCESS);
        buttonField.shouldHaveStyle("text-decoration: underline;");

        Checkbox checkbox = fields.field("Checkbox1").control(Checkbox.class);
        checkbox.shouldExists();
        checkbox.shouldNotBeChecked();
        checkbox.setChecked(true);
        buttonField.shouldHaveColor(ColorsEnum.DANGER);
        checkbox.setChecked(false);
        buttonField.shouldHaveColor(ColorsEnum.SUCCESS);

        buttonField = fields.field("Кнопка в контроле закругленная", ButtonField.class);
        buttonField.shouldExists();
        buttonField.shouldBeRounded();
    }

    @Test
    void testEnablingMessage() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/button_field/enabling_message/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        Checkbox checkbox = page.widget(FormWidget.class).fields().field("Условие доступности кнопки").control(Checkbox.class);
        checkbox.shouldExists();
        checkbox.shouldNotBeChecked();
        ButtonField buttonField = page.widget(FormWidget.class).fields().field("Кнопка-поле", ButtonField.class);
        buttonField.shouldExists();
        buttonField.shouldBeDisabled();
        buttonField.hover();
        buttonField.tooltip().shouldExists();
        buttonField.tooltip().shouldHaveText(new String[]{"Сообщение о причине недоступности"});
        checkbox.setChecked(true);
        buttonField.hover();
        buttonField.tooltip().shouldExists();
        buttonField.tooltip().shouldHaveText(new String[]{"Подсказка, если кнопка доступна"});
    }
}
