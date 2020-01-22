package net.n2oapp.framework.autotest;

import net.n2oapp.framework.autotest.component.control.InputControl;
import net.n2oapp.framework.autotest.component.control.SelectControl;
import net.n2oapp.framework.autotest.component.page.LeftRightPage;
import net.n2oapp.framework.autotest.component.page.SimplePage;
import net.n2oapp.framework.autotest.component.page.StandardPage;
import net.n2oapp.framework.autotest.component.region.PanelRegion;
import net.n2oapp.framework.autotest.component.region.TabsRegion;
import net.n2oapp.framework.autotest.component.widget.FormWidget;
import net.n2oapp.framework.autotest.component.widget.TableWidget;
import org.junit.Test;

import static net.n2oapp.framework.autotest.Matchers.whichContains;
import static net.n2oapp.framework.autotest.Matchers.whichEquals;
import static net.n2oapp.framework.autotest.Selectors.byId;

public class ApiTest {
    N2oAutotest n2o = new N2oAutotest();

    @Test
    public void testLeftRightPageAndFormAndInput() {
        n2o.open("/proto", LeftRightPage.class)
                .left()
                .region(0, PanelRegion.class)
                .content()
                .widget(0, FormWidget.class)
                .fields()
                .field("Имя")
                .control(InputControl.class)
                .shouldBeEnabled();

    }

    @Test
    public void testStandardPageAndTabsAndToolbar() {
        n2o.open("/proto", StandardPage.class)
                .place("single")
                .region(0, TabsRegion.class)
                .activeTab()
                .widget(0, FormWidget.class)
                .toolbar()
                .bottomRight()
                .button("Сохранить")
                .click();
    }

    @Test
    public void testTab() {
        n2o.open("/proto", SimplePage.class)
                .single()
                .widget(0, FormWidget.class)
                .fields()
                .row(0)
                .col(0)
                .field(byId("name"))
                .control(SelectControl.class)
                .select(whichContains("Мужской"));
    }

    @Test
    public void testPanel() {
        n2o.open("/proto", LeftRightPage.class)
                .right()
                .region(0, PanelRegion.class)
                .content()
                .widget(0, TableWidget.class)
                .columns()
                .rows()
                .row(0)
                .cell(0)
                .shouldHaveText(whichEquals("Joe"));
    }
}
