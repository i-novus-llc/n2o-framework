package net.n2oapp.framework.tutorial.crud.model;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;

/**
 * Стартовая страница Crud для автотестирования
 */
public class CrudPage {

    private final SimplePage simplePage;

    public CrudPage() {
        simplePage = N2oSelenide.page(SimplePage.class);
    }

    public TableWidget table() {
        return simplePage.widget(TableWidget.class);
    }

    public CreatePage create() {
        getButton(0).click();
        return new CreatePage();
    }

    public CreatePage update() {
        getButton(1).click();
        return new CreatePage();
    }

    public void delete() {
        getButton(2).click();
    }

    public void shouldDialogClosed(String title, long timeOut) {
        simplePage.dialog(title).shouldBeClosed(timeOut);
    }

    public void tableCellShouldHaveText(int row, int col, String text) {
        table().columns().rows().row(row).cell(col).textShouldHave(text);
    }

    public void shouldBeDialog(String title) {
        simplePage.dialog(title).shouldBeVisible();
    }

    public void acceptDialog(String title) {
        simplePage.dialog(title).click("Да");
    }

    public void tableAlertColorShouldBe(Colors colors) {
        table().alerts().alert(0).shouldHaveColor(colors);
    }

    private StandardButton getButton(Integer index) {
        return table().toolbar().topLeft().button(index, StandardButton.class);
    }
}
