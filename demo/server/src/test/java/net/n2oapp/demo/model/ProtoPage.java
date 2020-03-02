package net.n2oapp.demo.model;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.cell.*;
import net.n2oapp.framework.autotest.api.component.control.*;
import net.n2oapp.framework.autotest.api.component.page.LeftRightPage;
import net.n2oapp.framework.autotest.api.component.region.PanelRegion;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.list.ListWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.StandardTableHeader;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;

import java.util.List;

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
        getTableCell(rowNum, 4, TextCell.class).element().click();
    }

    public void tableCellShouldHaveText(int row, int col, String text) {
        getTable().columns().rows().row(row).cell(col).textShouldHave(text);
    }

    public void tableShouldSelectedRow(int row) {
        getTable().columns().rows().shouldBeSelected(row);
    }

    public void searchClients() {
        getTable().filters().search();
    }

    public void resetFilter() {
        getTable().filters().clear();
    }

    public StandardTableHeader getSurnameHeader() {
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

    public InputControl getSurnameFilter() {
        return getFilterFields().field("Фамилия").control(InputControl.class);
    }

    public InputControl getFirstNameFilter() {
        return getFilterFields().field("Имя").control(InputControl.class);
    }

    public void setBirthdayStartFilter(String value) {
        getFilterFields().field("Дата рождения").control(DateInterval.class).setBeginValue(value);
    }

    public void setBirthdayEndFilter(String value) {
        getFilterFields().field("Дата рождения").control(DateInterval.class).setEndValue(value);
    }

    public void getGenderColumnShouldHaveTexts(List<String> values) {
        getTable().columns().rows().columnShouldHaveTexts(4, values);
    }

    public List<String> getSurnameColumn() {
        return getTable().columns().rows().columnTexts(0);
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
        getTableCell(row, 0, LinkCell.class).click();
        return getProtoClient();
    }

    public ProtoClient clickNameCell(int row) {
        getTableCell(row, 1, LinkCell.class).click();
        return getModalProtoClient();
    }

    public ProtoClient clickPatronymicCell(int row) {
        getTableCell(row, 2, LinkCell.class).click();
        return getProtoClient();
    }

    public void clickBirthdayCell(int row) {
        getTableCell(row, 3, EditCell.class).click();
    }

    public void setVipCellChecked(int row) {
        getTableCell(row, 5, CheckboxCell.class).setChecked(true);
    }

    public void setVipCellNotChecked(int row) {
        getTableCell(row, 5, CheckboxCell.class).setChecked(false);
    }

    public void vipCellShouldBeChecked(int row) {
        getTableCell(row, 5, CheckboxCell.class).shouldBeChecked();
    }

    public void vipCellShouldNotBeChecked(int row) {
        getTableCell(row, 5, CheckboxCell.class).shouldBeUnchecked();
    }

    public DateInput getBirthdayCell(int row) {
        return getTableCell(row, 3, EditCell.class).control(DateInput.class);
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
        getTableCell(row, 6, ToolbarCell.class).clickMenu("Изменить");
        return getModalProtoClient();
    }

    public void deleteClientFromTableCell(int row) {
        getTableCell(row, 6, ToolbarCell.class).clickMenu("Удалить");
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

    public void acceptDialog(String title) {
        leftRightPage.dialog(title).click("Да");
    }

    public void contactsListShouldHaveText(int index, String text) {
        leftRightPage.right().region(0, PanelRegion.class).content().widget(ListWidget.class).content(index).body(TextCell.class).textShouldHave(text);
    }

    public ProtoContacts createContact() {
        leftRightPage.right().region(0, PanelRegion.class).content().widget(ListWidget.class).toolbar().topLeft().button("Создать").click();
        return getModalProtoContacts();
    }

    public InputControl getSurnameCard() {
        return getCardFields().field("Фамилия").control(InputControl.class);
    }

    public InputControl getFirstnameCard() {
        return getCardFields().field("Имя").control(InputControl.class);
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
        return leftRightPage.left().region(0, SimpleRegion.class).content().widget(TableWidget.class);
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
}
