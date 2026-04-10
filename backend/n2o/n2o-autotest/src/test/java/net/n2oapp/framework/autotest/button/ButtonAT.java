package net.n2oapp.framework.autotest.button;

import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.Tooltip;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.list.ListWidget;
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
 * Автотест для проверки кнопок
 */
class ButtonAT extends AutoTestBase {

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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    void testResolveAttributes() {
        setResourcePath("net/n2oapp/framework/autotest/button/resolve_attributes");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/button/resolve_attributes/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/resolve_attributes/reports.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/resolve_attributes/page.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        ListWidget list = page.widget(ListWidget.class);
        DropdownButton subMenu = list.toolbar().topLeft().dropdown();
        subMenu.shouldExists();
        subMenu.shouldHaveItems(2);
        subMenu.shouldHaveLabel("Все");
        subMenu.shouldHaveColor(ColorsEnum.SUCCESS);
        subMenu.hover();
        Tooltip tooltip = subMenu.tooltip();
        tooltip.shouldHaveText("text1");

        StandardButton item1 = subMenu.menuItem("Непрочитанные");
        StandardButton item2 = subMenu.menuItem("Все");

        StandardButton button = list.toolbar().topLeft().button("All");
        button.shouldHaveColor(ColorsEnum.PRIMARY);
        button.hover();
        tooltip = button.tooltip();
        tooltip.shouldHaveText("value1");

        subMenu.click();
        item1.shouldExists();
        item1.click();
        subMenu.shouldHaveLabel("Непрочитанные");
        subMenu.shouldHaveColor(ColorsEnum.DANGER);
        subMenu.hover();
        tooltip = subMenu.tooltip();
        tooltip.shouldHaveText("text2");

        button = list.toolbar().topLeft().button("Unread");
        button.shouldHaveColor(ColorsEnum.WARNING);
        button.hover();
        tooltip = button.tooltip();
        tooltip.shouldHaveText("value2");

        subMenu.click();
        item2.shouldExists();
        item2.click();
        subMenu.shouldHaveLabel("Все");
        subMenu.shouldHaveColor(ColorsEnum.SUCCESS);
        subMenu.hover();
        tooltip = subMenu.tooltip();
        tooltip.shouldHaveText("text1");

        button = list.toolbar().topLeft().button("All");
        button.shouldHaveColor(ColorsEnum.PRIMARY);
        button.hover();
        tooltip = button.tooltip();
        tooltip.shouldHaveText("value1");

        testRounded(list);
        testHoverExistence(list);
    }

    private void testRounded(ListWidget list) {
        StandardButton button;
        button = list.toolbar().topLeft().button("All");
        button.shouldExists();
        button.shouldNotBeRounded();
        button = list.toolbar().topLeft().button("rounded");
        button.shouldExists();
        button.shouldBeRounded();
    }

    private void testHoverExistence(ListWidget list) {
        StandardButton button = list.toolbar().topLeft().button("Открыть");
        button.shouldExists();
        button.hover();
        Tooltip tooltip = button.tooltip();
        tooltip.shouldHaveText("Подсказка должна пропасть после открытия и закрытия окна");
        button.click();
        Modal modal = N2oSelenide.modal();
        modal.shouldExists();
        modal.close();
        tooltip.shouldNotExists();
    }


    @Test
    void testButtonDisabledDuringActionExecution() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/button/disabled/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/disabled/data.object.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        form.shouldExists();

        ButtonField buttonField = form.fields().field("Сохранить (кнопка-поле)", ButtonField.class);
        buttonField.shouldBeEnabled();
        buttonField.click();
        buttonField.shouldBeDisabled();

        StandardButton fieldToolbarButton = form.fields().field("input").toolbar().button("Сохранить (тулбар поля)");
        fieldToolbarButton.shouldBeEnabled();
        fieldToolbarButton.click();
        fieldToolbarButton.shouldBeDisabled();

        StandardButton formToolbarButton = form.toolbar().bottomLeft().button("Сохранить (тулбар виджета)");
        formToolbarButton.shouldBeEnabled();
        formToolbarButton.click();
        formToolbarButton.shouldBeDisabled();

        StandardButton pageToolbarButton = page.toolbar().bottomLeft().button("Сохранить (тулбар страницы)");
        pageToolbarButton.shouldBeEnabled();
        pageToolbarButton.click();
        pageToolbarButton.shouldBeDisabled();

        // повторная проверка, чтобы учесть задержки
        buttonField.shouldBeDisabled();
        fieldToolbarButton.shouldBeDisabled();
        formToolbarButton.shouldBeDisabled();
        pageToolbarButton.shouldBeDisabled();
    }
}