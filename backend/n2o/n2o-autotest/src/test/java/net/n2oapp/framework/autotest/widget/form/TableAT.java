package net.n2oapp.framework.autotest.widget.form;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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
 * Автотест для виджета Таблица
 */
public class TableAT extends AutoTestBase {
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
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Test
    public void testTable() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/testTable.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget widget = page.single().widget(TableWidget.class);
        widget.filters().shouldBeVisible();
        widget.filters().toolbar().button("searchLabel").shouldBeEnabled();
        widget.filters().toolbar().button("resetLabel").shouldBeEnabled();
        widget.filters().fields().field("Имя").control(InputText.class).val("test");
        widget.filters().fields().field("Пол").control(Select.class).openOptions();
        widget.filters().fields().field("Пол").control(Select.class).select(Condition.text("Мужской"));
        widget.filters().toolbar().button("resetLabel").click();
        widget.filters().fields().field("Имя").control(InputText.class).shouldHaveValue("test");

        widget.toolbar().topRight().button(0, StandardButton.class).click();
        widget.filters().shouldBeInvisible();
        widget.toolbar().topRight().button(0, StandardButton.class).click();

        widget.columns().rows().row(0).cell(0).element().parent().shouldHave(Condition.cssClass("bg-danger"));
        widget.columns().rows().row(1).cell(0).element().parent().shouldHave(Condition.cssClass("bg-info"));
        widget.columns().rows().row(2).cell(0).element().parent().shouldHave(Condition.cssClass("bg-success"));
        widget.columns().headers().header(0).shouldHaveTitle("Имя");
        widget.columns().headers().header(1).shouldHaveTitle("Фамилия");
        widget.columns().headers().header(2).shouldHaveTitle("Дата рождения");

        widget.toolbar().topRight().button(1, DropdownButton.class).click();
        widget.toolbar().topRight().button(1, DropdownButton.class).menuItem("Имя").click();
        widget.columns().headers().header(0).shouldNotHaveTitle();
        widget.toolbar().topRight().button(1, DropdownButton.class).menuItem("Имя").click();
        widget.columns().headers().header(0).shouldHaveTitle("Имя");
    }
}
