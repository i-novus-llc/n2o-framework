package net.n2oapp.demo.model;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.cell.*;
import net.n2oapp.framework.autotest.api.component.control.*;
import net.n2oapp.framework.autotest.api.component.page.LeftRightPage;
import net.n2oapp.framework.autotest.api.component.region.PanelRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.list.ListWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableSimpleHeader;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Страница "Список контактов" ProtoPage.page.xml
 */
public class ProtoPage {

    private final LeftRightPage leftRightPage;

    public ProtoPage() {
        leftRightPage = N2oSelenide.page(LeftRightPage.class);
    }

    public void shouldBeClientsPage() {
        leftRightPage.breadcrumb().titleShouldHaveText("Список контактов");
    }

    public void tableShouldHaveSize(int size) {
        getTable().columns().rows().shouldHaveSize(size);
    }

    public void selectClient(int rowNum) {
        getTableCell(rowNum, 5, TextCell.class).element().click();
    }

    public void tableCellShouldHaveText(int row, int col, String text) {
        getTable().columns().rows().row(row).cell(col).textShouldHave(text);
    }

    public void tableCellShouldHaveText(int col, String text) {
        assertThat(getTable().columns().rows().columnTexts(col), hasItem(text));
    }

    public void tableShouldSelectedRow(int row) {
//        getTable().columns().rows().shouldBeSelected(row);  todo не поддерживаем selectedId
    }

    public void searchClients() {
        getTable().filters().search();
    }

    public void resetFilter() {
        getTable().filters().clear();
    }

    public TableSimpleHeader getSurnameHeader() {
        return getTable().columns().headers().header(Condition.text("Фамилия"));
    }

    public void genderFilterCheck(String label) {
        getFilterFields().field("Пол").control(CheckboxGroup.class).check(label);
    }

    public void genderFilterUncheck(String label) {
        getFilterFields().field("Пол").control(CheckboxGroup.class).uncheck(label);
    }

    public void genderFilterShouldBeChecked(String label) {
        getFilterFields().field("Пол").control(CheckboxGroup.class).shouldBeChecked(label);
    }

    public void genderFilterShouldBeUnchecked(String label) {
        getFilterFields().field("Пол").control(CheckboxGroup.class).shouldBeUnchecked(label);
    }

    public Checkbox getVIPFilter() {
        return getFilterFields().field("VIP").control(Checkbox.class);
    }

    public InputText getSurnameFilter() {
        return getFilterFields().field("Фамилия").control(InputText.class);
    }

    public InputText getFirstNameFilter() {
        return getFilterFields().field("Имя").control(InputText.class);
    }

    public void setBirthdayStartFilter(String value) {
        getFilterFields().field("Дата рождения").control(DateInterval.class).beginVal(value);
    }

    public void setBirthdayEndFilter(String value) {
        getFilterFields().field("Дата рождения").control(DateInterval.class).endVal(value);
    }

    public void getGenderColumnShouldHaveTexts(List<String> values) {
        getTable().columns().rows().columnShouldHaveTexts(5, values);
    }

    public List<String> getSurnameColumn() {
        return getTable().columns().rows().columnTexts(1);
    }

    public void currentPageShouldBe(String label) {
        getTable().paging().activePageShouldBe(label);
    }

    public void tableShouldHavePage(String number) {
        getTable().paging().pagingShouldHave(number);
    }

    public void selectPage(String number) {
        getTable().paging().selectPage(number);
    }

    public int getClientsCount() {
        return getTable().paging().totalElements();
    }

    public void clientsCountShouldBe(int count) {
        getTable().paging().totalElementsShouldBe(count);
    }

    public ProtoClient clickSurnameCell(int row) {
        getTableCell(row, 1, LinkCell.class).click();
        return getProtoClient();
    }

    public ProtoClient clickNameCell(int row) {
        getTableCell(row, 2, LinkCell.class).click();
        return getModalProtoClient();
    }

    public ProtoClient clickPatronymicCell(int row) {
        getTableCell(row, 3, LinkCell.class).click();
        return getProtoClient();
    }

    public void clickBirthdayCell(int row) {
        getTableCell(row, 4, EditCell.class).click();
    }

    public void setVipCellChecked(int row) {
        getTableCell(row, 6, CheckboxCell.class).setChecked(true);
    }

    public void setVipCellNotChecked(int row) {
        getTableCell(row, 6, CheckboxCell.class).setChecked(false);
    }

    public void vipCellShouldBeChecked(int row) {
        getTableCell(row, 6, CheckboxCell.class).shouldBeChecked();
    }

    public void vipCellShouldNotBeChecked(int row) {
        getTableCell(row, 6, CheckboxCell.class).shouldBeUnchecked();
    }

    public DateInput getBirthdayCell(int row) {
        return getTableCell(row, 4, EditCell.class).control(DateInput.class);
    }

    public ProtoClient addClient() {
        leftRightPage.toolbar().topRight().button("Добавить клиента").click();
        return getProtoClient();
    }

    public ProtoClient createClient() {
        getTable().toolbar().topLeft().button("Создать").click();
        return getModalProtoClient();
    }

    public ProtoClient editClientFromTableCell(int row) {
        DropdownButton dropdown = getTableCell(row, 7, ToolbarCell.class).toolbar().dropdown();
        dropdown.click();
        dropdown.menuItem("Изменить").click();
        return getModalProtoClient();
    }

    public void deleteClientFromTableCell(int row) {
        DropdownButton dropdown = getTableCell(row, 7, ToolbarCell.class).toolbar().dropdown();
        dropdown.click();
        dropdown.menuItem("Удалить").click();
    }

    public void deleteClientFromTableToolBar() {
        getTable().toolbar().topLeft().button("Удалить").click();
    }

    public ProtoClient editClientFromTableToolBar() {
        getTable().toolbar().topLeft().button("Изменить").click();
        return getModalProtoClient();
    }

    public ProtoClient viewClientFromTableToolBar() {
        getTable().toolbar().topRight().button("Просмотр").click();
        return getModalProtoClient();
    }

    public void shouldBeDialog(String title) {
        leftRightPage.dialog(title).shouldBeVisible();
    }

    public void shouldDialogHaveText(String title, String text) {
        leftRightPage.dialog(title).shouldHaveText(text);
    }

    public void shouldDialogClosed(String title, long timeOut) {
        leftRightPage.dialog(title).shouldBeClosed(timeOut);
    }

    public void shouldDialogClosed(String title) {
        shouldDialogClosed(title, 5000);
    }

    public void acceptDialog(String title) {
        leftRightPage.dialog(title).click("Да");
    }

    public void contactsListShouldHaveText(int index, String text) {
        getContacts().content(index).body(TextCell.class).textShouldHave(text);
    }

    public void contactsListShouldHaveSize(int size) {
        getContacts().shouldHaveSize(size);
    }

    public ProtoContacts createContact() {
        getContacts().toolbar().topLeft().button("Создать").click();
        return getModalProtoContacts();
    }

    public ProtoContacts editContact(int index) {
        ListCell cell = getContacts().content(index).extra(ListCell.class);
        cell.element().$$(".btn").findBy(Condition.text("Изменить")).click();
        return getModalProtoContacts();
    }

    public void alertTextShouldBe(String text) {
//        leftRightPage.alerts().alert(0).shouldHaveText(text); todo
    }

    public void alertColorShouldBe(Colors colors) {
//        leftRightPage.alerts().alert(0).shouldHaveColor(colors); todo
    }

    public void contactsAlertTextShouldBe(String text) {
//        getContacts().alerts().alert(0).shouldHaveText(text); todo
    }

    public void contactsAlertColorShouldBe(Colors colors) {
//        getContacts().alerts().alert(0).shouldHaveColor(colors); todo
    }

    public InputText getSurnameCard() {
        return getCardFields().field("Фамилия").control(InputText.class);
    }

    public InputText getFirstnameCard() {
        return getCardFields().field("Имя").control(InputText.class);
    }

    public RadioGroup getGenderCard() {
        return getCardFields().field("Пол").control(RadioGroup.class);
    }

    public Checkbox getVIPCard() {
        return getCardFields().field("VIP").control(Checkbox.class);
    }

    public ProtoClient getProtoClient() {
        return new ProtoClient();
    }

    public ProtoClient getModalProtoClient() {
        return new ProtoClient(true);
    }

    public ProtoContacts getModalProtoContacts() {
        return new ProtoContacts(true);
    }

    private TableWidget getTable() {
        return leftRightPage.left().region(0, PanelRegion.class).content().widget(TableWidget.class);
    }

    private <T extends Cell> T getTableCell(int row, int col, Class<T> componentClass) {
        return getTable().columns().rows().row(row).cell(col, componentClass);
    }

    private Fields getFilterFields() {
        return getTable().filters().fields();
    }

    private Fields getCardFields() {
        return leftRightPage.right().region(1, PanelRegion.class).content().widget(FormWidget.class).fields();
    }

    private ListWidget getContacts() {
        return leftRightPage.right().region(0, PanelRegion.class).content().widget(ListWidget.class);
    }
}
