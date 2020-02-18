package net.n2oapp.framework.autotest;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.InputControl;
import net.n2oapp.framework.autotest.api.component.control.SelectControl;
import net.n2oapp.framework.autotest.api.component.page.LeftRightPage;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.PanelRegion;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.TableWidget;

public class ApiExample {

    public void testLeftRightPageAndFormAndInput() {
        N2oSelenide.open("/#/proto", LeftRightPage.class)
                .left()
                .region(0, PanelRegion.class)
                .content()
                .widget(0, FormWidget.class)
                .fields()
                .field("Имя")
                .control(InputControl.class)
                .shouldBeEnabled();

    }

    public void testStandardPageAndTabsAndToolbar() {
        N2oSelenide.open("/#/proto", StandardPage.class)
                .place("single")
                .region(0, TabsRegion.class)
                .activeTab()
                .widget(0, FormWidget.class)
                .toolbar()
                .bottomRight()
                .button("Сохранить")
                .click();
    }

    public void testTab() {
        N2oSelenide.open("/#/proto", SimplePage.class)
                .single()
                .widget(0, FormWidget.class)
                .fields()
                .field(Condition.id("name"))
                .control(SelectControl.class)
                .select(Condition.text("Мужской"));
    }

    public void testPanel() {
        N2oSelenide.open("/#/proto", LeftRightPage.class)
                .right()
                .region(0, PanelRegion.class)
                .content()
                .widget(0, TableWidget.class)
                .columns()
                .rows()
                .row(0)
                .cell(0)
                .textShouldHave("Joe");
    }
}
