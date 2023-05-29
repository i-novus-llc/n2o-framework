package net.n2oapp.demo;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import net.n2oapp.demo.model.ProtoClient;
import net.n2oapp.demo.model.ProtoContacts;
import net.n2oapp.demo.model.ProtoPage;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.SortingDirection;
import net.n2oapp.framework.autotest.Colors;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.Configuration.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

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
        try (CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build()) {
            HttpResponse httpResponse = closeableHttpClient.execute(request);
            assertThat(httpResponse.getCode(), equalTo(HttpStatus.SC_OK));
        }
    }

    /**
     * Проверка работы фильтра по полу
     */
    @Test
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
        protoPage.resetFilter();
    }

    /**
     * Проверка работы фильтра по фамилии и имени
     */
    @Test
    public void testFilterByNameAndSurname() {
        protoPage.getSurnameFilter().shouldBeEnabled();
        protoPage.getFirstNameFilter().shouldBeEnabled();
        protoPage.getSurnameFilter().click();
        protoPage.getSurnameFilter().setValue("Лапа");
        protoPage.getFirstNameFilter().click();
        protoPage.getFirstNameFilter().setValue("ера");
        protoPage.searchClients();

        protoPage.tableShouldHaveSize(1);
        protoPage.tableCellShouldHaveText(0, 1, "Лапаева");
        protoPage.tableCellShouldHaveText(0, 2, "Вера");
        protoPage.resetFilter();
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
        protoPage.tableCellShouldHaveText(1, "Кручинина");
        protoPage.tableCellShouldHaveText(1, "Мишин");
        protoPage.resetFilter();
    }

    /**
     * Проверка работы очистки фильтра
     */
    @Test
    public void testClearFilter() {
        protoPage.getFirstNameFilter().click();
        protoPage.getFirstNameFilter().setValue("Римма");
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
    @Disabled
    public void testTableSorting() {
        List<String> unsortedSurnameColumn = new ArrayList<>(
                List.of(new String[]{
                        "Михалёва",
                        "Яблочкин",
                        "Яшнова",
                        "Глоба",
                        "Суходолина",
                        "Барсова",
                        "Оленева",
                        "Меркушев",
                        "Летова",
                        "Чечин",
                })
        );

        protoPage.getSurnameHeader().shouldNotBeSorted();
        protoPage.getSurnameHeader().click();
        protoPage.getSurnameHeader().shouldBeSortedByAsc();
        protoPage.surnameColumnShouldBeSortedBy(SortingDirection.ASC);

        protoPage.getSurnameHeader().click();
        protoPage.getSurnameHeader().shouldBeSortedByDesc();
        protoPage.surnameColumnShouldBeSortedBy(SortingDirection.DESC);

        protoPage.getSurnameHeader().click();
        protoPage.getSurnameHeader().shouldNotBeSorted();
        protoPage.surnameColumnShouldNotBeSorted(unsortedSurnameColumn);
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

        protoPage.selectPage("1");
    }

    /**
     * Проверка работы ячейки VIP (2 строка)
     */
    @Test
    public void testVipCellUpdate() {
        protoPage.vipCellShouldBeChecked(2);
        protoPage.setVipCellNotChecked(2);
        protoPage.alertColorShouldBe(Colors.SUCCESS);
        protoPage.alertTextShouldBe("Успешно обновлены данные клиента с фамилией");
        protoPage.vipCellShouldNotBeChecked(2);
        protoPage.setVipCellChecked(2);
        protoPage.alertColorShouldBe(Colors.SUCCESS);
        protoPage.alertTextShouldBe("Успешно обновлены данные клиента с фамилией");
        protoPage.vipCellShouldBeChecked(2);
    }

    /**
     * Проверка редактирования даты в таблице (3 строка)
     */
    @Test
    public void testCellBirthdayUpdate() {
        int row = 0;
        String testDate = "15.12.1900";
        String exDate = protoPage.getBirthdayCell(row).getValue();

        protoPage.getBirthdayCell(row).shouldHaveValue(exDate);
        protoPage.clickBirthdayCell(row);
        protoPage.getBirthdayCell(row).shouldHaveValue(exDate);
        protoPage.getBirthdayCell(row).setValue(testDate);
        protoPage.alertColorShouldBe(Colors.SUCCESS);
        protoPage.getBirthdayCell(row).shouldHaveValue(testDate);

        protoPage.clickBirthdayCell(row);
        protoPage.getBirthdayCell(row).setValue(exDate);
        protoPage.alertColorShouldBe(Colors.SUCCESS);
        protoPage.getBirthdayCell(row).shouldHaveValue(exDate);
    }

    /**
     * Проверка работы ячейки Фамилия   (4 строка)
     */
    @Test
    public void testSurnameCellUpdate() {
        int row = 3;
        String surname = protoPage.getSurname(row);
        String name = protoPage.getName(row);
        String patronomic = protoPage.getPatronomic(row);
        String birthday = protoPage.getBirthdayCell(row).getValue();
        String gender = protoPage.getGender(row);

        ProtoClient clientCard = protoPage.clickSurnameCell(row);
        clientCard.shouldHaveTitle("Карточка клиента");

        clientCard.surname().shouldHaveValue(surname);
        clientCard.firstName().shouldHaveValue(name);
        clientCard.patronymic().shouldHaveValue(patronomic);
        clientCard.genderRadioGroup().shouldBeChecked(gender);
        clientCard.birthdayShouldHaveValue(birthday);

        clientCard.surname().click();
        clientCard.surname().setValue("Сергеев");
        clientCard.firstName().click();
        clientCard.firstName().setValue("Николай");
        clientCard.patronymic().click();
        clientCard.patronymic().setValue("Петрович");
        clientCard.save();

        protoPage.shouldBeClientsPage();
        protoPage.alertColorShouldBe(Colors.SUCCESS);
        protoPage.alertTextShouldBe("Успешно обновлены данные клиента с фамилией Сергеев");
        protoPage.tableShouldSelectedRow(row);
        protoPage.tableCellShouldHaveText(row, 1, "Сергеев");
        protoPage.tableCellShouldHaveText(row, 2, "Николай");
        protoPage.tableCellShouldHaveText(row, 3, "Петрович");
    }

    /**
     * Проверка работы ячейки Имя (5 строка)
     */
    @Test
    public void testNameCellUpdate() {
        int row = 4;
        String surname = protoPage.getSurname(row);
        String name = protoPage.getName(row);
        String patronomic = protoPage.getPatronomic(row);
        String gender = protoPage.getGender(row);
        String birthday = protoPage.getBirthdayCell(row).getValue();

        ProtoClient modalClientCard = protoPage.clickNameCell(row);
        modalClientCard.shouldHaveTitle("Карточка клиента:");

        modalClientCard.surname().shouldHaveValue(surname);
        modalClientCard.firstName().shouldHaveValue(name);
        modalClientCard.patronymic().shouldHaveValue(patronomic);
        modalClientCard.genderRadioGroup().shouldBeChecked(gender);
        modalClientCard.birthdayShouldHaveValue(birthday);

        modalClientCard.surname().click();
        modalClientCard.surname().setValue("Александринкин");
        modalClientCard.firstName().click();
        modalClientCard.firstName().setValue("Иннокентута");
        modalClientCard.patronymic().click();
        modalClientCard.patronymic().setValue("Игнатиевич");
        modalClientCard.gender().check("Мужской");
        modalClientCard.save();

        protoPage.shouldDialogClosed("Карточка клиента:");
        protoPage.shouldBeClientsPage();
        protoPage.alertColorShouldBe(Colors.SUCCESS);
        protoPage.alertTextShouldBe("Успешно обновлены данные клиента с фамилией Александринкин");
        protoPage.tableShouldSelectedRow(row);
        protoPage.tableCellShouldHaveText(row, 1, "Александринкин");
        protoPage.tableCellShouldHaveText(row, 2, "Иннокентута");
        protoPage.tableCellShouldHaveText(row, 3, "Игнатиевич");
    }

    /**
     * Проверка работы ячейки Отчество  (6я строка)
     */
    @Test
    public void testPatronymicCellUpdate() {
        int row = 5;
        String surname = protoPage.getSurname(row);
        String name = protoPage.getName(row);
        String patronomic = protoPage.getPatronomic(row);
        String gender = protoPage.getGender(row);
        String birthday = protoPage.getBirthdayCell(row).getValue();

        ProtoClient clientCard = protoPage.clickPatronymicCell(row);
        clientCard.shouldHaveTitle("Карточка клиента");

        clientCard.surname().shouldHaveValue(surname);
        clientCard.firstName().shouldHaveValue(name);
        clientCard.patronymic().shouldHaveValue(patronomic);
        clientCard.genderRadioGroup().shouldBeChecked(gender);
        clientCard.birthdayShouldHaveValue(birthday);

        clientCard.surname().click();
        clientCard.surname().setValue("Сергеева");
        clientCard.firstName().click();
        clientCard.firstName().setValue("Анастасия");
        clientCard.patronymic().click();
        clientCard.patronymic().setValue("Михайловна");
        clientCard.save();

        protoPage.shouldBeClientsPage();
        protoPage.alertColorShouldBe(Colors.SUCCESS);
        protoPage.alertTextShouldBe("Успешно обновлены данные клиента с фамилией Сергеева");
        protoPage.tableShouldSelectedRow(row);
        protoPage.tableCellShouldHaveText(row, 1, "Сергеева");
        protoPage.tableCellShouldHaveText(row, 2, "Анастасия");
        protoPage.tableCellShouldHaveText(row, 3, "Михайловна");
    }

    /**
     * Проверка изменения клиента через модальное окно
     */
    @Test
    public void testUpdateClient() {
        int row = 6;
        String surname = protoPage.getSurname(row);
        String name = protoPage.getName(row);
        String patronomic = protoPage.getPatronomic(row);
        String gender = protoPage.getGender(row);
        String birthday = protoPage.getBirthdayCell(row).getValue();

        protoPage.selectClient(row);

        ProtoClient modalClientCard = protoPage.editClientFromTableToolBar();
        modalClientCard.shouldHaveTitle("Карточка клиента:");

        modalClientCard.surname().shouldHaveValue(surname);
        modalClientCard.firstName().shouldHaveValue(name);
        modalClientCard.patronymic().shouldHaveValue(patronomic);
        modalClientCard.birthdayShouldHaveValue(birthday);
        modalClientCard.genderRadioGroup().shouldBeChecked(gender);

        modalClientCard.surname().click();
        modalClientCard.surname().setValue("Жуков");
        modalClientCard.firstName().click();
        modalClientCard.firstName().setValue("Геннадий");
        modalClientCard.patronymic().click();
        modalClientCard.patronymic().setValue("Юрьевич");
        modalClientCard.gender().check("Мужской");
        modalClientCard.birthdayValue("17.11.1932");
        modalClientCard.save();

        protoPage.shouldDialogClosed("Карточка клиента:");
        protoPage.shouldBeClientsPage();
        protoPage.alertColorShouldBe(Colors.SUCCESS);
        protoPage.alertTextShouldBe("Успешно обновлены данные клиента с фамилией Жуков");
        protoPage.tableShouldSelectedRow(row);
        protoPage.tableCellShouldHaveText(row, 1, "Жуков");
        protoPage.tableCellShouldHaveText(row, 2, "Геннадий");
        protoPage.tableCellShouldHaveText(row, 3, "Юрьевич");
        protoPage.tableCellShouldHaveText(row, 4, "17.11.1932");
        protoPage.tableCellShouldHaveText(row, 5, "Мужской");
    }

    /**
     * Проверка изменения клиента через меню в ячейке
     */
    @Test
    public void testUpdateClientFromToolbarCell() {
        int row = 7;
        String surname = protoPage.getSurname(row);
        String name = protoPage.getName(row);
        String patronomic = protoPage.getPatronomic(row);
        String gender = protoPage.getGender(row);
        String birthday = protoPage.getBirthdayCell(row).getValue();

        ProtoClient modalClientCard = protoPage.editClientFromTableCell(row);
        modalClientCard.shouldHaveTitle("Клиент - Изменение");

        modalClientCard.surname().shouldHaveValue(surname);
        modalClientCard.firstName().shouldHaveValue(name);
        modalClientCard.patronymic().shouldHaveValue(patronomic);
        modalClientCard.genderRadioGroup().shouldBeChecked(gender);
        modalClientCard.birthdayShouldHaveValue(birthday);

        modalClientCard.surname().click();
        modalClientCard.surname().setValue("Ивановна");
        modalClientCard.firstName().click();
        modalClientCard.firstName().setValue("Александра");
        modalClientCard.patronymic().click();
        modalClientCard.patronymic().setValue("Петровна");
        modalClientCard.gender().check("Женский");
        modalClientCard.save();

        protoPage.shouldDialogClosed("Клиент - Изменение");
        protoPage.shouldBeClientsPage();
        protoPage.alertColorShouldBe(Colors.SUCCESS);
        protoPage.alertTextShouldBe("Успешно обновлены данные клиента с фамилией Иванова");
        protoPage.tableShouldSelectedRow(row);
        protoPage.tableCellShouldHaveText(row, 1, "Ивановна");
        protoPage.tableCellShouldHaveText(row, 2, "Александра");
        protoPage.tableCellShouldHaveText(row, 3, "Петровна");
        protoPage.tableCellShouldHaveText(row, 4, birthday);
        protoPage.tableCellShouldHaveText(row, 5, "Женский");
    }

    /**
     * Просмотр клиента через модальное окно (1я строка)
     */
    @Test
    public void testViewClient() {
        int row = 2;
        String surname = protoPage.getSurname(row);
        String name = protoPage.getName(row);
        String patronomic = protoPage.getPatronomic(row);
        String gender = protoPage.getGender(row);
        String birthday = protoPage.getBirthdayCell(row).getValue();

        protoPage.selectClient(row);
        ProtoClient modalClientCard = protoPage.clickView();
        modalClientCard.shouldHaveTitle("Просмотр клиента");

        modalClientCard.surname().shouldHaveValue(surname);
        modalClientCard.firstName().shouldHaveValue(name);
        modalClientCard.patronymic().shouldHaveValue(patronomic);
        modalClientCard.birthdayShouldHaveValue(birthday);
//        modalClientCard.gender().shouldHaveValue(gender); todo not work in disabled mode

        modalClientCard.surname().shouldBeDisabled();
        modalClientCard.firstName().shouldBeDisabled();
        modalClientCard.patronymic().shouldBeDisabled();
        modalClientCard.birthdayShouldBeDisabled();
//        modalClientCard.gender().shouldBeDisabled(); todo not work in disabled mode
        modalClientCard.getVIP().shouldBeDisabled();

        modalClientCard.close();

        protoPage.shouldDialogClosed("Просмотр клиента");
    }


    /**
     * Проверка создания клиента
     */
    @Test
    public void testPageCreateClient() {
        ProtoClient clientCard = protoPage.addClient();
        clientCard.shouldHaveTitle("Карточка клиента");
        clientCard.patronymic().shouldHaveValue("Тест");
        clientCard.surname().click();
        clientCard.surname().setValue("Гадойбоев");
        clientCard.firstName().click();
        clientCard.firstName().setValue("Муминджон");
        clientCard.patronymic().click();
        clientCard.patronymic().setValue("Джамшутович");
        clientCard.genderRadioGroup().check("Мужской");
        clientCard.birthdayValue("17.01.2020");
        clientCard.getVIP().setChecked(true);
        clientCard.save();

        protoPage.shouldBeClientsPage();
        protoPage.alertColorShouldBe(Colors.SUCCESS);
        protoPage.alertTextShouldBe("Клиент 'Гадойбоев' создан");

        protoPage.getSurnameFilter().click();
        protoPage.getSurnameFilter().setValue("Гадойбоев");
        protoPage.searchClients();

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
    public void testModalCreateClient() {
        ProtoClient modalClientCard = protoPage.createClient();
        modalClientCard.shouldHaveTitle("Карточка клиента");
        modalClientCard.patronymic().shouldHaveValue("Тест");
        modalClientCard.surname().click();
        modalClientCard.surname().setValue("Иконов");
        modalClientCard.firstName().click();
        modalClientCard.firstName().setValue("Алексей");
        modalClientCard.patronymic().click();
        modalClientCard.patronymic().setValue("Петрович");
        modalClientCard.genderRadioGroup().check("Мужской");
        modalClientCard.birthdayValue("17.01.2020");
        modalClientCard.getVIP().setChecked(true);
        modalClientCard.save();

        protoPage.shouldDialogClosed("Карточка клиента");

        ProtoClient clientCard = protoPage.getProtoClient();
        clientCard.shouldHaveTitle("Карточка клиента");
        clientCard.surname().shouldHaveValue("Иконов");
        clientCard.firstName().shouldHaveValue("Алексей");
        clientCard.patronymic().shouldHaveValue("Петрович");
        clientCard.genderRadioGroup().shouldBeChecked("Мужской");
        clientCard.birthdayShouldHaveValue("17.01.2020");
        clientCard.getVIP().shouldBeChecked();
        clientCard.close();
        protoPage.shouldBeClientsPage();

        protoPage.getSurnameFilter().click();
        protoPage.getSurnameFilter().setValue("Иконов");
        protoPage.searchClients();

        protoPage.tableCellShouldHaveText(0, 1, "Иконов");
        protoPage.tableCellShouldHaveText(0, 2, "Алексей");
        protoPage.tableCellShouldHaveText(0, 3, "Петрович");
        protoPage.tableCellShouldHaveText(0, 4, "17.01.2020");
        protoPage.tableCellShouldHaveText(0, 5, "Мужской");
        protoPage.vipCellShouldBeChecked(0);
    }

    /**
     * Тест удаления клиента (предпоследняя строка) из тулбара в колонке
     */
    @Test
    public void testTableInPlaceDelete() {
        int row = 8;
        protoPage.tableShouldHaveSize(10);
        int count = protoPage.getClientsCount();
        List<String> surnames = protoPage.getSurnameColumn();

        protoPage.deleteClientFromTableCell(row);

        protoPage.shouldBeDialog("Предупреждение");
        protoPage.shouldDialogHaveText("Предупреждение", "Вы уверены, что хотите удалить клиента " + surnames.get(row));
        protoPage.acceptDialog("Предупреждение");

        protoPage.alertColorShouldBe(Colors.SUCCESS);
        protoPage.alertTextShouldBe("Клиент " + surnames.get(row) + " удален");
        protoPage.clientsCountShouldBe(count - 1);
    }

    /**
     * Тест удаления клиента (предпоследняя строка) из тулбара таблицы
     */

    @Test
    public void testTableRowDelete() {
        int row = 8;
        protoPage.tableShouldHaveSize(10);
        int count = protoPage.getClientsCount();
        List<String> surnames = protoPage.getSurnameColumn();

        protoPage.selectClient(row);
        protoPage.deleteClientFromTableToolBar();

        protoPage.shouldBeDialog("Предупреждение");
        protoPage.shouldDialogHaveText("Предупреждение", "Вы уверены, что хотите удалить клиента " + surnames.get(row));
        protoPage.acceptDialog("Предупреждение");

        protoPage.alertColorShouldBe(Colors.SUCCESS);
        protoPage.alertTextShouldBe("Клиент " + surnames.get(row) + " удален");
        protoPage.clientsCountShouldBe(count - 1);
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
        protoPage.tableCellShouldHaveText(5, 1, "Сергеева");
        protoPage.contactsListShouldHaveText(0, "+7950267859");
        protoPage.getSurnameCard().shouldHaveValue("Сергеева");
        protoPage.getFirstnameCard().shouldHaveValue("Анастасия");
        protoPage.getGenderCard().shouldBeChecked("Женский");
        protoPage.getVIPCard().shouldBeChecked();
    }

    /**
     * Проверка создания контакта
     */
    @Test
    public void testContactCrud() {
        protoPage.getSurnameFilter().click();
        protoPage.getSurnameFilter().setValue("Маркин");
        protoPage.searchClients();
        protoPage.tableCellShouldHaveText(0, 1, "Маркин");
        protoPage.contactsListShouldHaveSize(0);

        ProtoContacts modalProtoContacts = protoPage.createContact();
        modalProtoContacts.shouldHaveTitle("Контакты");
        modalProtoContacts.selectContactType("Мобильный телефон");
        modalProtoContacts.getPhoneNumber().setValue("9999999999");
        modalProtoContacts.getDescription().click();
        modalProtoContacts.getDescription().setValue("рабочий телефон");
        modalProtoContacts.save();

        protoPage.shouldDialogClosed("Контакты", 20000);
        protoPage.shouldBeClientsPage();
        protoPage.contactsAlertColorShouldBe(Colors.SUCCESS);
        protoPage.contactsAlertTextShouldBe("Данные сохранены");

        protoPage.contactsListShouldHaveSize(1);
        protoPage.contactsListShouldHaveText(0, "+7 (999) 999-99-99");

        modalProtoContacts = protoPage.editContact(0);
        modalProtoContacts.shouldHaveTitle("Контакты");
        modalProtoContacts.shouldHaveContactType("Мобильный телефон");
        modalProtoContacts.getPhoneNumber().shouldHaveValue("+7 (999) 999-99-99");
        modalProtoContacts.getPhoneNumber().setValue("8888888888");
        modalProtoContacts.getPhoneNumber().shouldHaveValue("+7 (888) 888-88-88");
        modalProtoContacts.save();

        protoPage.shouldDialogClosed("Контакты", 10000);
        protoPage.shouldBeClientsPage();
        protoPage.contactsAlertColorShouldBe(Colors.SUCCESS);
        protoPage.contactsAlertTextShouldBe("Данные сохранены");

        protoPage.contactsListShouldHaveText(0, "+7 (888) 888-88-88");

        protoPage.deleteContact(0);
        protoPage.contactsListShouldHaveSize(0);
    }
}
