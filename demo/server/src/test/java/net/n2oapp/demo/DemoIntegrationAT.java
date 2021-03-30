package net.n2oapp.demo;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import net.n2oapp.demo.model.ProtoClient;
import net.n2oapp.demo.model.ProtoContacts;
import net.n2oapp.demo.model.ProtoPage;
import net.n2oapp.framework.autotest.Colors;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.Configuration.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoApplication.class, properties = {"n2o.i18n.enabled=false", "n2o.i18n.default-locale=ru"},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DemoIntegrationAT {

    @LocalServerPort
    private int port;

    private ProtoPage protoPage;

    @BeforeAll
    public static void configure() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        System.setProperty("chromeoptions.args", "--no-sandbox,--verbose,--whitelisted-ips=''");

        headless = true;
        browserSize = "1920x1200";
        timeout = 10000;
    }

    @BeforeEach
    public void openProtoPage() {
        protoPage = Selenide.open("http://localhost:" + port, ProtoPage.class);
        protoPage.shouldBeClientsPage();
    }

    /**
     * Проверка отдачи статики
     */
    @Test
    @Order(1)
    public void checkStaticContent() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:" + port + "/index.html");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        assertThat(httpResponse.getCode(), equalTo(HttpStatus.SC_OK));
    }

    /**
     * Проверка работы фильтра по полу
     */
    @Test
    @Order(2)
    public void testFilterByGender() {
        protoPage.genderFilterCheck("Женский");
        protoPage.genderFilterShouldBeUnchecked("Мужской");
        protoPage.genderFilterShouldBeChecked("Женский");

        protoPage.searchClients();
        protoPage.getGenderColumnShouldHaveTexts(Collections.nCopies(10, "Женский"));

        protoPage.genderFilterUncheck("Женский");
        protoPage.genderFilterCheck("Мужской");
        protoPage.genderFilterShouldBeChecked("Мужской");
        protoPage.genderFilterShouldBeUnchecked("Женский");

        protoPage.searchClients();
        protoPage.getGenderColumnShouldHaveTexts(Collections.nCopies(10, "Мужской"));

        protoPage.genderFilterUncheck("Мужской");
        protoPage.genderFilterCheck("Не определенный");
        protoPage.genderFilterShouldBeChecked("Не определенный");
        protoPage.genderFilterShouldBeUnchecked("Мужской");

        protoPage.searchClients();
        protoPage.getGenderColumnShouldHaveTexts(Collections.emptyList());
    }

    /**
     * Проверка работы фильтра по фамилии и имени
     */
    @Test
    @Order(3)
    public void testFilterByNameAndSurname() {
        protoPage.getSurnameFilter().shouldBeEnabled();
        protoPage.getFirstNameFilter().shouldBeEnabled();
        protoPage.getSurnameFilter().val("Лапа");
        protoPage.getFirstNameFilter().val("ера");
        protoPage.searchClients();

        protoPage.tableShouldHaveSize(1);
        protoPage.tableCellShouldHaveText(0, 1, "Лапаева");
        protoPage.tableCellShouldHaveText(0, 2, "Вера");
    }

    /**
     * Проверка работы фильтра по дате рождения
     */
    @Test
    public void testFilterByBirthday() {
        protoPage.setBirthdayStartFilter("01.01.1940");
        protoPage.setBirthdayEndFilter("01.12.1940");
        protoPage.searchClients();

        protoPage.tableShouldHaveSize(2);
        protoPage.tableCellShouldHaveText(0, 1, "Кручинина");
        protoPage.tableCellShouldHaveText(1, 1, "Мишин");
    }

    /**
     * Проверка работы очистки фильтра
     */
    @Test
    public void testClearFilter() {
        protoPage.getFirstNameFilter().val("Римма");
        protoPage.genderFilterCheck("Женский");
        protoPage.getVIPFilter().setChecked(true);

        protoPage.getFirstNameFilter().shouldHaveValue("Римма");
        protoPage.genderFilterShouldBeChecked("Женский");
        protoPage.getVIPFilter().shouldBeChecked();

        protoPage.searchClients();
        protoPage.tableShouldHaveSize(2);

        protoPage.resetFilter();
        protoPage.tableShouldHaveSize(10);

        protoPage.getFirstNameFilter().shouldHaveValue("");
        protoPage.genderFilterShouldBeUnchecked("Женский");
        protoPage.getVIPFilter().shouldBeEmpty();
    }


    /**
     * Проверка работы сортировки по фамилии
     */
    @Test
    public void testTableSorting() {
        protoPage.getSurnameHeader().shouldNotBeSorted();
        protoPage.getSurnameHeader().click();
        protoPage.getSurnameHeader().shouldBeSortedByAsc();
        assertThat(isSorted(protoPage.getSurnameColumn(), true), is(true));

        protoPage.getSurnameHeader().click();
        protoPage.getSurnameHeader().shouldBeSortedByDesc();
        assertThat(isSorted(protoPage.getSurnameColumn(), false), is(true));

        protoPage.getSurnameHeader().click();
        protoPage.getSurnameHeader().shouldNotBeSorted();
        List<String> list = protoPage.getSurnameColumn();
        assertThat(isSorted(list, true), is(false));
        assertThat(isSorted(list, false), is(false));
    }

    /**
     * Проверка корректности работы пагинации
     */
    @Test
    public void testPagination() {
        protoPage.tableShouldHaveSize(10);
        protoPage.currentPageShouldBe("1");
        protoPage.tableShouldHavePage("2");
        protoPage.selectPage("2");

        protoPage.tableShouldHaveSize(10);
        protoPage.currentPageShouldBe("2");
        protoPage.tableShouldHavePage("1");
        protoPage.tableShouldHavePage("3");
    }

    /**
     * Проверка работы ячейки Фамилия   (4я строка)
     */
    @Test
    public void testSurnameCell() {
        ProtoClient clientCard = protoPage.clickSurnameCell(4);
        clientCard.shouldHaveTitle("Карточка клиента");

        clientCard.surname().shouldHaveValue("Вольваков");
        clientCard.firstName().shouldHaveValue("Вениамин");
        clientCard.patronymic().shouldHaveValue("Тихонович");
        clientCard.genderRadioGroup().shouldBeChecked("Мужской");
        clientCard.birthdayShouldHaveValue("04.06.1932");
        clientCard.getVIP().shouldBeChecked();

        clientCard.surname().val("Сергеев");
        clientCard.firstName().val("Николай");
        clientCard.patronymic().val("Петрович");
        clientCard.save();

        protoPage.shouldBeClientsPage();
        protoPage.tableAlertColorShouldBe(Colors.SUCCESS);
        protoPage.tableAlertTextShouldBe("Успешно обновлены данные клиента с фамилией Сергеев");
        protoPage.tableShouldSelectedRow(4);
        protoPage.tableCellShouldHaveText(4, 1, "Сергеев");
        protoPage.tableCellShouldHaveText(4, 2, "Николай");
        protoPage.tableCellShouldHaveText(4, 3, "Петрович");
    }

    /**
     * Проверка работы ячейки Имя
     */
    @Test
    public void testNameCell() {
        protoPage.getSurnameHeader().click();
        ProtoClient modalClientCard = protoPage.clickNameCell(1);

        modalClientCard.shouldHaveTitle("Карточка клиента:");

        modalClientCard.surname().shouldHaveValue("Александрин");
        modalClientCard.firstName().shouldHaveValue("Иннокентий");
        modalClientCard.patronymic().shouldHaveValue("Игнатиевич");
        modalClientCard.genderRadioGroup().shouldBeChecked("Мужской");
        modalClientCard.birthdayShouldHaveValue("11.04.1994");
        modalClientCard.getVIP().shouldBeChecked();

        modalClientCard.surname().val("Александринкин");
        modalClientCard.firstName().val("Иннокентута");
        modalClientCard.patronymic().val("Игнатиевичя");
        modalClientCard.save();

        protoPage.shouldDialogClosed("Карточка клиента:");
        protoPage.shouldBeClientsPage();
        protoPage.tableAlertColorShouldBe(Colors.SUCCESS);
        protoPage.tableAlertTextShouldBe("Успешно обновлены данные клиента с фамилией Александринкин");
        protoPage.tableShouldSelectedRow(1);
        protoPage.tableCellShouldHaveText(1, 1, "Александринкин");
        protoPage.tableCellShouldHaveText(1, 2, "Иннокентута");
        protoPage.tableCellShouldHaveText(1, 3, "Игнатиевичя");
    }

    /**
     * Проверка работы ячейки Отчество  (7я строка)
     */
    @Test
    public void testPatronymicCell() {
        ProtoClient clientCard = protoPage.clickPatronymicCell(7);
        clientCard.shouldHaveTitle("Карточка клиента");

        clientCard.surname().shouldHaveValue("Иванова");
        clientCard.firstName().shouldHaveValue("Зинаида");
        clientCard.patronymic().shouldHaveValue("Виталиевна");
        clientCard.genderRadioGroup().shouldBeChecked("Женский");
        clientCard.birthdayShouldHaveValue("11.09.1933");
        clientCard.getVIP().shouldBeChecked();

        clientCard.surname().val("Сергеева");
        clientCard.firstName().val("Анастасия");
        clientCard.patronymic().val("Михайловна");
        clientCard.save();

        protoPage.shouldBeClientsPage();
        protoPage.tableAlertColorShouldBe(Colors.SUCCESS);
        protoPage.tableAlertTextShouldBe("Успешно обновлены данные клиента с фамилией Сергеева");
        protoPage.tableShouldSelectedRow(7);
        protoPage.tableCellShouldHaveText(7, 1, "Сергеева");
        protoPage.tableCellShouldHaveText(7, 2, "Анастасия");
        protoPage.tableCellShouldHaveText(7, 3, "Михайловна");
    }

    /**
     * Проверка работы ячейки VIP
     */
    @Test
    public void testVipCell() {
        protoPage.vipCellShouldBeChecked(2);
        protoPage.setVipCellNotChecked(2);
        protoPage.tableAlertColorShouldBe(Colors.SUCCESS);
        protoPage.tableAlertTextShouldBe("Успешно обновлены данные клиента с фамилией");
        protoPage.vipCellShouldNotBeChecked(2);
        protoPage.setVipCellChecked(2);
        protoPage.tableAlertColorShouldBe(Colors.SUCCESS);
        protoPage.tableAlertTextShouldBe("Успешно обновлены данные клиента с фамилией");
        protoPage.vipCellShouldBeChecked(2);
    }

    /**
     * Проверка редактирования даты в таблице
     */
    @Test
    public void testTableEditBirthday() {
        String testDate = "01.01.1800";
        String exDate = protoPage.getBirthdayCell(0).val();

        protoPage.getBirthdayCell(0).shouldHaveValue(exDate);
        protoPage.clickBirthdayCell(0);
        protoPage.getBirthdayCell(0).shouldHaveValue(exDate);
        protoPage.getBirthdayCell(0).val(testDate);
        protoPage.tableAlertColorShouldBe(Colors.SUCCESS);
        protoPage.tableAlertTextShouldBe("Успешно обновлены данные клиента с фамилией Плюхина");
        protoPage.getBirthdayCell(0).shouldHaveValue(testDate);

        protoPage.clickBirthdayCell(0);
        protoPage.getBirthdayCell(0).val(exDate);
        protoPage.tableAlertColorShouldBe(Colors.SUCCESS);
        protoPage.tableAlertTextShouldBe("Успешно обновлены данные клиента с фамилией Плюхина");
        protoPage.getBirthdayCell(0).shouldHaveValue(exDate);
    }

    /**
     * Проверка создания клиента
     */
    @Test
    public void testAddClient() {
        ProtoClient clientCard = protoPage.addClient();
        clientCard.shouldHaveTitle("Карточка клиента");
        clientCard.patronymic().shouldHaveValue("Тест");
        clientCard.surname().val("Гадойбоев");
        clientCard.firstName().val("Муминджон");
        clientCard.patronymic().val("Джамшутович");
        clientCard.genderRadioGroup().check("Мужской");
        clientCard.birthdayValue("17.01.2020");
        clientCard.getVIP().setChecked(true);
        clientCard.save();

        protoPage.shouldBeClientsPage();
        protoPage.tableAlertColorShouldBe(Colors.SUCCESS);
        protoPage.tableAlertTextShouldBe("Клиент 'Гадойбоев' создан");
        protoPage.tableShouldSelectedRow(0);
        protoPage.tableCellShouldHaveText(0, 1, "Гадойбоев");
        protoPage.tableCellShouldHaveText(0, 2, "Муминджон");
        protoPage.tableCellShouldHaveText(0, 3, "Джамшутович");
        protoPage.tableCellShouldHaveText(0, 4, "17.01.2020");
        protoPage.tableCellShouldHaveText(0, 5, "Мужской");
        protoPage.vipCellShouldBeChecked(0);
    }

    /**
     * Проверка создания клиента через модальное окно
     */
    @Test
    public void testCreateClient() {
        ProtoClient modalClientCard = protoPage.createClient();
        modalClientCard.shouldHaveTitle("Карточка клиента");
        modalClientCard.patronymic().shouldHaveValue("Тест");
        modalClientCard.surname().val("Иванов");
        modalClientCard.firstName().val("Алексей");
        modalClientCard.patronymic().val("Петрович");
        modalClientCard.genderRadioGroup().check("Мужской");
        modalClientCard.birthdayValue("17.01.2020");
        modalClientCard.getVIP().setChecked(true);
        modalClientCard.save();

        protoPage.shouldDialogClosed("Карточка клиента");

        ProtoClient clientCard = protoPage.getProtoClient();
        clientCard.shouldHaveTitle("Карточка клиента");
        clientCard.surname().shouldHaveValue("Иванов");
        clientCard.firstName().shouldHaveValue("Алексей");
        clientCard.patronymic().shouldHaveValue("Петрович");
        clientCard.genderRadioGroup().shouldBeChecked("Мужской");
        clientCard.birthdayShouldHaveValue("17.01.2020");
        clientCard.getVIP().shouldBeChecked();
        clientCard.close();

        protoPage.shouldBeClientsPage();
        protoPage.tableShouldSelectedRow(0);
        protoPage.tableCellShouldHaveText(0, 1, "Иванов");
        protoPage.tableCellShouldHaveText(0, 2, "Алексей");
        protoPage.tableCellShouldHaveText(0, 3, "Петрович");
        protoPage.tableCellShouldHaveText(0, 4, "17.01.2020");
        protoPage.tableCellShouldHaveText(0, 5, "Мужской");
        protoPage.vipCellShouldBeChecked(0);
    }

    /**
     * Проверка изменения клиента через модальное окно  (3я строка)
     */
    @Test
    public void testUpdateClient() {
        protoPage.selectClient(3);

        protoPage.tableCellShouldHaveText(3, 1, "Сиянкин");
        protoPage.tableCellShouldHaveText(3, 2, "Мир");
        protoPage.tableCellShouldHaveText(3, 3, "Григориевич");
        protoPage.tableCellShouldHaveText(3, 4, "02.05.1930");
        protoPage.tableCellShouldHaveText(3, 5, "Мужской");
        protoPage.vipCellShouldBeChecked(3);

        ProtoClient modalClientCard = protoPage.editClientFromTableToolBar();
        modalClientCard.shouldHaveTitle("Карточка клиента:");

        modalClientCard.surname().shouldHaveValue("Сиянкин");
        modalClientCard.firstName().shouldHaveValue("Мир");
        modalClientCard.patronymic().shouldHaveValue("Григориевич");
        modalClientCard.birthdayShouldHaveValue("02.05.1930");
        modalClientCard.genderRadioGroup().shouldBeChecked("Мужской");
        modalClientCard.getVIP().shouldBeChecked();

        modalClientCard.surname().val("Жуков");
        modalClientCard.firstName().val("Геннадий");
        modalClientCard.patronymic().val("Юрьевич");
        modalClientCard.save();

        protoPage.shouldDialogClosed("Карточка клиента:");
        protoPage.shouldBeClientsPage();
        protoPage.tableAlertColorShouldBe(Colors.SUCCESS);
        protoPage.tableAlertTextShouldBe("Успешно обновлены данные клиента с фамилией Жуков");
        protoPage.tableShouldSelectedRow(3);
        protoPage.tableCellShouldHaveText(3, 1, "Жуков");
        protoPage.tableCellShouldHaveText(3, 2, "Геннадий");
        protoPage.tableCellShouldHaveText(3, 3, "Юрьевич");
        protoPage.tableCellShouldHaveText(3, 4, "02.05.1930");
        protoPage.tableCellShouldHaveText(3, 5, "Мужской");
        protoPage.vipCellShouldBeChecked(3);
    }

    /**
     * Проверка изменения клиента через меню в ячейке (2я строка)
     */
    @Test
    public void testUpdateClientFromToolbarCell() {
        protoPage.tableCellShouldHaveText(2, 1, "Пищикова");
        protoPage.tableCellShouldHaveText(2, 2, "Нина");
        protoPage.tableCellShouldHaveText(2, 3, "Никитевна");
        protoPage.tableCellShouldHaveText(2, 4, "10.05.1929");
        protoPage.tableCellShouldHaveText(2, 5, "Женский");
        protoPage.vipCellShouldBeChecked(2);

        ProtoClient modalClientCard = protoPage.editClientFromTableCell(2);
        modalClientCard.shouldHaveTitle("Клиент - Изменение");

        modalClientCard.surname().shouldHaveValue("Пищикова");
        modalClientCard.firstName().shouldHaveValue("Нина");
        modalClientCard.patronymic().shouldHaveValue("Никитевна");
        modalClientCard.genderRadioGroup().shouldBeChecked("Женский");
        modalClientCard.birthdayShouldHaveValue("10.05.1929");
        modalClientCard.getVIP().shouldBeChecked();

        modalClientCard.surname().val("Иванова");
        modalClientCard.firstName().val("Александра");
        modalClientCard.patronymic().val("Петровна");
        modalClientCard.save();

        protoPage.shouldDialogClosed("Клиент - Изменение");
        protoPage.shouldBeClientsPage();
        protoPage.tableAlertColorShouldBe(Colors.SUCCESS);
        protoPage.tableAlertTextShouldBe("Успешно обновлены данные клиента с фамилией Иванова");
        protoPage.tableShouldSelectedRow(2);
        protoPage.tableCellShouldHaveText(2, 1, "Иванова");
        protoPage.tableCellShouldHaveText(2, 2, "Александра");
        protoPage.tableCellShouldHaveText(2, 3, "Петровна");
        protoPage.tableCellShouldHaveText(2, 4, "10.05.1929");
        protoPage.tableCellShouldHaveText(2, 5, "Женский");
        protoPage.vipCellShouldBeChecked(2);
    }

    /**
     * Просмотр клиента через модальное окно (1я строка)
     */
    @Test
    public void testViewClient() {
        protoPage.selectClient(1);

        protoPage.tableCellShouldHaveText(1, 1, "Маркин");
        protoPage.tableCellShouldHaveText(1, 2, "Семен");
        protoPage.tableCellShouldHaveText(1, 3, "Демьянович");
        protoPage.tableCellShouldHaveText(1, 4, "25.03.1929");
        protoPage.tableCellShouldHaveText(1, 5, "Мужской");
        protoPage.vipCellShouldNotBeChecked(1);

        ProtoClient modalClientCard = protoPage.viewClientFromTableToolBar();
        modalClientCard.shouldHaveTitle("Просмотр клиента");

        modalClientCard.surname().shouldHaveValue("Маркин");
        modalClientCard.firstName().shouldHaveValue("Семен");
        modalClientCard.patronymic().shouldHaveValue("Демьянович");
        modalClientCard.birthdayShouldHaveValue("25.03.1929");
        modalClientCard.gender().shouldSelected("Мужской");
        modalClientCard.getVIP().shouldBeEmpty();

        modalClientCard.surname().shouldBeDisabled();
        modalClientCard.firstName().shouldBeDisabled();
        modalClientCard.patronymic().shouldBeDisabled();
        modalClientCard.birthdayShouldBeDisabled();
        modalClientCard.gender().shouldBeDisabled();
        modalClientCard.getVIP().shouldBeDisabled();

        modalClientCard.close();

        protoPage.shouldDialogClosed("Просмотр клиента");
        protoPage.shouldBeClientsPage();
        protoPage.tableShouldSelectedRow(1);
    }

    /**
     * Тест удаления клиента (предпоследняя строка) из тулбара в колонке
     */
    @Test
    @Order(4)
    public void testTableInPlaceDelete() {
        protoPage.tableShouldHaveSize(10);
        int count = protoPage.getClientsCount();
        List<String> surnames = protoPage.getSurnameColumn();

        protoPage.deleteClientFromTableCell(8);

        protoPage.shouldBeDialog("Предупреждение");
        protoPage.shouldDialogHaveText("Предупреждение", "Вы уверены, что хотите удалить клиента " + surnames.get(8));
        protoPage.acceptDialog("Предупреждение");

        protoPage.tableAlertColorShouldBe(Colors.SUCCESS);
        protoPage.tableAlertTextShouldBe("Клиент " + surnames.get(8) + " удален");
        protoPage.clientsCountShouldBe(count - 1);
        List<String> nSurnames = protoPage.getSurnameColumn();
        for (int i = 0; i < 8; i++) {
            assertThat(surnames.get(i), is(nSurnames.get(i)));
        }
        assertThat(surnames.get(9), is(nSurnames.get(8)));
    }

    /**
     * Тест удаления клиента (предпоследняя строка) из тулбара таблицы
     */
    @Test
    public void testTableRowDelete() {
        protoPage.tableShouldHaveSize(10);
        int count = protoPage.getClientsCount();
        List<String> surnames = protoPage.getSurnameColumn();

        protoPage.selectClient(8);
        protoPage.deleteClientFromTableToolBar();

        protoPage.shouldBeDialog("Предупреждение");
        protoPage.shouldDialogHaveText("Предупреждение", "Вы уверены, что хотите удалить клиента " + surnames.get(8));
        protoPage.acceptDialog("Предупреждение");

        protoPage.tableAlertColorShouldBe(Colors.SUCCESS);
        protoPage.tableAlertTextShouldBe("Клиент " + surnames.get(8) + " удален");
        protoPage.clientsCountShouldBe(count - 1);
        List<String> nSurnames = protoPage.getSurnameColumn();
        for (int i = 0; i < 8; i++) {
            assertThat(surnames.get(i), is(nSurnames.get(i)));
        }
        assertThat(surnames.get(9), is(nSurnames.get(8)));
    }

    /**
     * Проверка master-detail связи (5,6 строки)
     */
    @Test
    public void testMasterDetail() {
        protoPage.selectClient(6);
        protoPage.tableCellShouldHaveText(6, 1, "Чуканова");
        protoPage.contactsListShouldHaveText(0, "3333333");
        protoPage.getSurnameCard().shouldHaveValue("Чуканова");
        protoPage.getFirstnameCard().shouldHaveValue("Изольда");
        protoPage.getGenderCard().shouldBeChecked("Женский");
        protoPage.getVIPCard().shouldBeChecked();

        protoPage.selectClient(5);
        protoPage.tableCellShouldHaveText(5, 1, "Дуванова");
        protoPage.contactsListShouldHaveText(0, "+7950267859");
        protoPage.getSurnameCard().shouldHaveValue("Дуванова");
        protoPage.getFirstnameCard().shouldHaveValue("Ольга");
        protoPage.getGenderCard().shouldBeChecked("Женский");
        protoPage.getVIPCard().shouldBeChecked();
    }

    /**
     * Проверка создания контакта
     */
    @Test
    @Order(5)
    public void testCreateContact() {
        protoPage.getSurnameFilter().val("Маркин");
        protoPage.searchClients();
        protoPage.tableCellShouldHaveText(0, 1, "Маркин");

        ProtoContacts modalProtoContacts = protoPage.createContact();
        modalProtoContacts.shouldHaveTitle("Контакты");
        modalProtoContacts.selectContactType("Мобильный телефон");
        modalProtoContacts.getPhoneNumber().val("9999999999");
        modalProtoContacts.getDescription().val("рабочий телефон");
        modalProtoContacts.save();

        protoPage.shouldDialogClosed("Контакты", 20000);
        protoPage.shouldBeClientsPage();
        protoPage.contactsAlertColorShouldBe(Colors.SUCCESS);
        protoPage.contactsAlertTextShouldBe("Данные сохранены");
        protoPage.tableShouldHaveSize(1);

        protoPage.contactsListShouldHaveText(0, "+7 (999) 999-99-99");
    }

    /**
     * Проверка редактирования контакта после testCreateContact!
     */
    @Test
    @Order(6)
    public void testEditContact() {
        protoPage.getSurnameFilter().val("Маркин");
        protoPage.searchClients();
        protoPage.tableCellShouldHaveText(0, 1, "Маркин");
        protoPage.contactsListShouldHaveText(0, "+7 (999) 999-99-99");

        ProtoContacts modalProtoContacts = protoPage.editContact(0);
        modalProtoContacts.shouldHaveTitle("Контакты");
        modalProtoContacts.shouldHaveContactType("Мобильный телефон");
        modalProtoContacts.getPhoneNumber().shouldHaveValue("+7 (999) 999-99-99");
        modalProtoContacts.getPhoneNumber().val("8888888888");
        modalProtoContacts.getPhoneNumber().shouldHaveValue("+7 (888) 888-88-88");
        modalProtoContacts.save();

        protoPage.shouldDialogClosed("Контакты", 10000);
        protoPage.shouldBeClientsPage();
        protoPage.contactsAlertColorShouldBe(Colors.SUCCESS);
        protoPage.contactsAlertTextShouldBe("Данные сохранены");
        protoPage.tableShouldHaveSize(1);

        protoPage.contactsListShouldHaveText(0, "+7 (888) 888-88-88");
    }

    private boolean isSorted(List<String> list, Boolean dir) {
        for (int i = 0; i < list.size() - 1; i++) {
            if ((dir && list.get(i).compareTo(list.get(i + 1)) >= 0)
                    || (!dir && list.get(i).compareTo(list.get(i + 1)) <= 0)) return false;
        }
        return true;
    }
}
