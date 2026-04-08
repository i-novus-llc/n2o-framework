package net.n2oapp.framework.autotest.region;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.collection.Regions;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.drawer.Drawer;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.region.SubPageRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * Автотест для региона подстраниц
 */
class SubPageRegionAT extends AutoTestBase {
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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());

    }

    @Test
    void testSubPageWithButtons() {
        setResourcePath("net/n2oapp/framework/autotest/region/subpage/with_buttons");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/with_buttons/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/with_buttons/audio.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/with_buttons/friends.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/with_buttons/user.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/with_buttons/userInfo.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/with_buttons/video.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/with_buttons/test.query.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.columns().rows().row(1).click();
        table.toolbar().topRight().button("open-page").click();

        StandardPage userPage = N2oSelenide.page(StandardPage.class);
        userPage.breadcrumb().crumb(1).shouldHaveLabel("Страница пользователя");
        FormWidget form = userPage.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        Toolbar toolbar = form.toolbar().bottomLeft();

        SimplePage subPage = userPage.regions().region(1, SubPageRegion.class).content(SimplePage.class);
        // При открытии должен быть редирект на default-page
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/2/info");
        form = subPage.widget(FormWidget.class);
        form.fields().field("userInfo").control(OutputText.class).shouldHaveValue("USER INFO");
        userPage.breadcrumb().crumb(2).shouldHaveLabel("userInfo sub-page");

        toolbar.button("Друзья").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/2/friends");
        form.fields().field("friends").control(OutputText.class).shouldHaveValue("FRIENDS");
        userPage.breadcrumb().crumb(2).shouldHaveLabel("friends sub-page");

        toolbar.button("Инфо").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/2/info");
        userPage.breadcrumb().crumb(2).shouldHaveLabel("info sub-page");

        toolbar.button("Аудиозаписи").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/2/audio");
        userPage.breadcrumb().crumb(2).shouldHaveLabel("audio sub-page");

        toolbar.button("Видеозаписи").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/2/video");
        userPage.breadcrumb().crumb(2).shouldHaveLabel("video sub-page");

        userPage.breadcrumb().crumb(1).click();
        userPage.breadcrumb().crumb(2).shouldHaveLabel("userInfo sub-page");
    }

    @Test
    void testSubPageWithoutButtons() {
        setResourcePath("net/n2oapp/framework/autotest/region/subpage/without_buttons");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/without_buttons/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/without_buttons/audio.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/without_buttons/friends.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/without_buttons/user.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/without_buttons/userInfo.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/without_buttons/video.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/without_buttons/test.query.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.columns().rows().row(2).click();
        table.toolbar().topRight().button("open-page").click();

        StandardPage userPage = N2oSelenide.page(StandardPage.class);
        userPage.breadcrumb().crumb(1).shouldHaveLabel("Страница пользователя");

        SimplePage subPage = userPage.regions().region(0, SubPageRegion.class).content(SimplePage.class);
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/3/info");
        FormWidget form = subPage.widget(FormWidget.class);
        form.fields().field("userInfo").control(OutputText.class).shouldHaveValue("USER INFO");

        Toolbar toolbar = form.toolbar().bottomLeft();
        toolbar.button("Вперед").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/3/friends");
        form.fields().field("friends").control(OutputText.class).shouldHaveValue("FRIENDS");

        toolbar.button("Назад").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/3/info");

        toolbar.button("Вперед").click();
        toolbar.button("Вперед").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/3/audio");

        toolbar.button("Вперед").click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/3/video");
    }

    @Test
    void testSubPageInModal() {
        setResourcePath("net/n2oapp/framework/autotest/region/subpage/modal");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/modal/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/modal/audio.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/modal/friends.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/modal/user.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/modal/userInfo.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/modal/video.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/modal/test.query.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.columns().rows().row(1).click();
        table.toolbar().topRight().button("show-modal").click();

        Modal modal = N2oSelenide.modal();
        StandardPage userPage = modal.content(StandardPage.class);
        userPage.shouldExists();

        FormWidget form = userPage.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        Toolbar toolbar = form.toolbar().bottomLeft();

        SimplePage subPage = userPage.regions().region(1, SubPageRegion.class).content(SimplePage.class);
        form = subPage.widget(FormWidget.class);
        form.fields().field("userInfo").control(OutputText.class).shouldHaveValue("USER INFO");

        toolbar.button("Друзья").click();
        form.fields().field("friends").control(OutputText.class).shouldHaveValue("FRIENDS");

        toolbar.button("Инфо").click();
        form.fields().field("userInfo").control(OutputText.class).shouldHaveValue("USER INFO");

        toolbar.button("Аудиозаписи").click();
        form.fields().field("audio").control(OutputText.class).shouldHaveValue("AUDIO");

        toolbar.button("Видеозаписи").click();
        form.fields().field("video").control(OutputText.class).shouldHaveValue("VIDEO");
    }

    /**
     * Проверяет попадание routable-параметров виджетов (фильтры, сортировки, пагинация),
     * расположенных в subpage, в адресную строку страницы
     */
    @Test
    void testSubPageRoutableParameters() {
        setResourcePath("net/n2oapp/framework/autotest/region/subpage/routable");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/routable/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/routable/friends.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/routable/friendsQuery.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/routable/user.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/routable/info.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/routable/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        // 1. Попадание routable параметров виджетов (фильтры, сортировки, пагинация), расположенных в subpage в адресную строку страницы
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.columns().rows().row(1).click();
        table.toolbar().topRight().button("open-page").click();

        StandardPage userPage = N2oSelenide.page(StandardPage.class);
        SimplePage subPage = userPage.regions().region(1, SubPageRegion.class).content(SimplePage.class);
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/user/2/info");

        FormWidget form = userPage.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        form.toolbar().bottomLeft().button("Друзья").click();
        TableWidget friendsTable = subPage.widget(TableWidget.class);
        friendsTable.shouldExists();
        subPage.shouldHaveUrlMatches(".*/#/user/2/friends.*");

        // фильтр
        InputText cityFilter = friendsTable.filters().fields().field("city").control(InputText.class);
        cityFilter.setValue("Москва");
        friendsTable.filters().toolbar().button(SEARCH_BUTTON_LABEL).click();
        String cityRus = URLEncoder.encode("Москва", StandardCharsets.UTF_8);
        subPage.shouldHaveUrlMatches(".*city=" + cityRus + ".*");
        friendsTable.paging().shouldHaveTotalElements(6);

        // сортировка
        friendsTable.columns().headers().header(2).click();
        subPage.shouldHaveUrlMatches(".*sorting_friendsDs_city=ASC.*");

        // пагинация
        friendsTable.paging().shouldHaveActivePage("1");
        friendsTable.paging().selectPage("2");
        subPage.shouldHaveUrlMatches(".*page=2.*");

        // проверка после обновления страницы
        Selenide.refresh();
        subPage.shouldHaveUrlMatches(".*/#/user/2/friends.*");
        subPage.shouldHaveUrlMatches(".*city=" + cityRus + ".*");
        subPage.shouldHaveUrlMatches(".*sorting_friendsDs_city=ASC.*");
        subPage.shouldHaveUrlMatches(".*page=2.*");
        friendsTable.columns().headers().header(2).shouldBeSortedByAsc();
        friendsTable.paging().shouldHaveActivePage("2");
        cityFilter.shouldHaveValue("Москва");
        friendsTable.paging().shouldHaveTotalElements(6);

        // 2. routable параметры виджетов в модалках не должны влиять на адресную строку страницы
        form.toolbar().bottomLeft().button("На главную").click();
        table.toolbar().topRight().button("show-modal").click();

        Modal modal = N2oSelenide.modal();
        userPage = modal.content(StandardPage.class);
        userPage.shouldExists();

        subPage = userPage.regions().region(1, SubPageRegion.class).content(SimplePage.class);
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/");

        form = userPage.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        form.toolbar().bottomLeft().button("Друзья").click();
        friendsTable = subPage.widget(TableWidget.class);
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/");

        // фильтр
        cityFilter = friendsTable.filters().fields().field("city").control(InputText.class);
        cityFilter.setValue("Москва");
        friendsTable.filters().toolbar().button(SEARCH_BUTTON_LABEL).click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/");
        friendsTable.paging().shouldHaveTotalElements(6);

        // сортировка
        friendsTable.columns().headers().header(2).click();
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/");

        // пагинация
        friendsTable.paging().shouldHaveActivePage("1");
        friendsTable.paging().selectPage("2");
        subPage.shouldHaveUrlMatches(getBaseUrl() + "/#/");
    }

    /**
     * Проверяет проброс query-параметров в subpage
     */
    @Test
    void testSubPageQueryParameters() {
        setResourcePath("net/n2oapp/framework/autotest/region/subpage/query");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/query/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/query/page.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/query/subpage.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/subpage/query/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        page.toolbar().topRight().button("Открыть страницу").click();
        StandardPage userPage = N2oSelenide.page(StandardPage.class);
        userPage.shouldExists();
        checkSubPageQueryParameters(userPage.regions(), "test2", "page");
        userPage.breadcrumb().crumb(0).click();

        page.toolbar().topRight().button("Открыть страницу через ссылку").click();
        userPage = N2oSelenide.page(StandardPage.class);
        userPage.shouldExists();
        checkSubPageQueryParameters(userPage.regions(), "test3", "link");
        userPage.breadcrumb().crumb(0).click();

        page.toolbar().topRight().button("Открыть drawer").click();
        Drawer drawer = N2oSelenide.drawer();
        drawer.shouldExists();
        Regions regions = drawer.content(StandardPage.class).regions();
        checkSubPageQueryParameters(regions, "test4", "drawer");
        drawer.close();

        page.toolbar().topRight().button("Открыть modal").click();
        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldExists();
        regions = modalPage.content(StandardPage.class).regions();
        checkSubPageQueryParameters(regions, "test5", "modal");
    }

    private static void checkSubPageQueryParameters(Regions regions, String filterValue, String paramValue) {
        Fields fields = regions.region(0, SimpleRegion.class).content().widget(FormWidget.class).fields();
        RegionItems subPageContent = regions.region(1, SubPageRegion.class).content(StandardPage.class)
                .regions().region(0, SimpleRegion.class).content();
        fields.field("Фильтр таблицы").control(InputText.class).shouldHaveValue(filterValue);
        fields.field("Тестовый параметр").control(InputText.class).shouldHaveValue(paramValue);
        TableWidget table = subPageContent.widget(TableWidget.class);
        table.filters().fields().field("name").control(InputText.class).shouldHaveValue(filterValue);
        table.columns().rows().row(0).cell(1).shouldHaveText(filterValue);
        subPageContent.widget(1, FormWidget.class).fields()
                .field("Тестовый параметр на subpage").control(InputText.class).shouldHaveValue(paramValue);
    }
}