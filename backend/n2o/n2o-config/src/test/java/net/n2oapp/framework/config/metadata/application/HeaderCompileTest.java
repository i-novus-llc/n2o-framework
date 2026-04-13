package net.n2oapp.framework.config.metadata.application;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.header.Header;
import net.n2oapp.framework.api.metadata.header.MenuItem;
import net.n2oapp.framework.api.metadata.header.SearchBar;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции заголовка
 */
class HeaderCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        builder.getEnvironment().getContextProcessor().set("username", "test");
        builder.properties("n2o.homepage.id=index");
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oApplicationPack(), new N2oQueriesPack(),
                new N2oActionsPack());
    }

    @Test
    void inlineMenu() {
        Application application = compile("net/n2oapp/framework/config/metadata/menu/pageWithoutLabel.page.xml",
                "net/n2oapp/framework/config/metadata/application/testPage.page.xml",
                "net/n2oapp/framework/config/metadata/application/headerWithMenu.application.xml")
                .get(new ApplicationContext("headerWithMenu"));

        Header header = application.getHeader();

        assertThat(header.getMenu().getItems().size(), is(4));
        List<MenuItem> menuItems = header.getMenu().getItems();
        // sub-menu non ds
        assertThat(menuItems.getFirst().getTitle(), is("test"));
        assertThat(menuItems.getFirst().getDatasource(), is("ds1"));
        assertThat(menuItems.getFirst().getSubItems().size(), is(2));
        assertThat(menuItems.getFirst().getSrc(), is("DropdownMenuItem"));
        // page
        assertThat(menuItems.get(1).getTitle(), is("headerLabel"));
        assertThat(menuItems.get(1).getDatasource(), is("ds1"));
        assertThat(menuItems.get(1).getHref(), is("/mi4"));
        assertThat(menuItems.get(1).getPageId(), is("pageWithoutLabel"));
        assertThat(menuItems.get(1).getLinkType(), is(MenuItem.LinkTypeEnum.INNER));
        assertThat(menuItems.get(1).getSubItems(), nullValue());
        assertThat(menuItems.get(1).getSrc(), is("LinkMenuItem"));
        // a
        assertThat(menuItems.get(2).getTitle(), is("hrefLabel"));
        assertThat(menuItems.get(2).getDatasource(), is("ds2"));
        assertThat(menuItems.get(2).getHref(), is("http://test.com"));
        assertThat(menuItems.get(2).getLinkType(), is(MenuItem.LinkTypeEnum.OUTER));
        assertThat(menuItems.get(2).getSubItems(), nullValue());
        assertThat(menuItems.get(2).getSrc(), is("LinkMenuItem"));
        // sub-menu with ds
        assertThat(menuItems.get(3).getTitle(), is("`test`"));
        assertThat(menuItems.get(3).getDatasource(), is("ds2"));
        assertThat(menuItems.get(3).getSubItems().size(), is(2));
        assertThat(menuItems.get(3).getModel(), is(ReduxModelEnum.MULTI));

        checkSubItems(header, menuItems);
    }

    private void checkSubItems(Header header, List<MenuItem> menuItems) {
        ArrayList<MenuItem> subItems = menuItems.get(3).getSubItems();
        assertThat(subItems.getFirst().getTitle(), is("test"));
        assertThat(subItems.getFirst().getDatasource(), is("ds1"));
        assertThat(subItems.getFirst().getModel(), is(ReduxModelEnum.EDIT));
        assertThat(subItems.get(1).getTitle(), is("`test`"));
        assertThat(subItems.get(1).getDatasource(), is("ds2"));
        assertThat(subItems.get(1).getModel(), is(ReduxModelEnum.MULTI));

        subItems = menuItems.getFirst().getSubItems();
        // sub-menu page
        assertThat(subItems.getFirst().getTitle(), is("test2"));
        assertThat(subItems.getFirst().getDatasource(), is("ds2"));
        assertThat(subItems.getFirst().getModel(), is(ReduxModelEnum.DATASOURCE));
        assertThat(subItems.getFirst().getHref(), is("/page1"));
        assertThat(subItems.getFirst().getPageId(), is("pageWithoutLabel"));
        assertThat(subItems.getFirst().getLinkType(), is(MenuItem.LinkTypeEnum.INNER));
        assertThat(subItems.getFirst().getSubItems(), nullValue());
        assertThat(subItems.getFirst().getSrc(), is("LinkMenuItem"));
        assertThat(subItems.getFirst().getProperties().size(), is(1));
        assertThat(subItems.getFirst().getProperties().get("attr1"), is("testAttribute"));
        // sub-menu a
        assertThat(subItems.get(1).getTitle(), is("hrefLabel"));
        assertThat(subItems.get(1).getDatasource(), is("ds1"));
        assertThat(subItems.get(1).getHref(), is("http://test.com"));
        assertThat(subItems.get(1).getLinkType(), is(MenuItem.LinkTypeEnum.OUTER));
        assertThat(subItems.get(1).getSubItems(), nullValue());
        assertThat(subItems.get(1).getSrc(), is("LinkMenuItem"));
        assertThat(header.getExtraMenu().getItems().size(), is(1));
        MenuItem extraItem = header.getExtraMenu().getItems().getFirst();
        // sub-menu
        assertThat(extraItem.getTitle(), is("#{username}"));
        assertThat(extraItem.getSubItems().size(), is(1));
        assertThat(extraItem.getSrc(), is("DropdownMenuItem"));
        subItems = extraItem.getSubItems();
        // sub-menu a
        assertThat(subItems.getFirst().getTitle(), is("Test"));
        assertThat(subItems.getFirst().getHref(), is("https://ya.ru/"));
        assertThat(subItems.getFirst().getLinkType(), is(MenuItem.LinkTypeEnum.OUTER));
        assertThat(subItems.getFirst().getIcon(), is("test-icon"));
        assertThat(subItems.getFirst().getSubItems(), nullValue());
        assertThat(subItems.getFirst().getSrc(), is("LinkMenuItem"));
    }

    @Test
    void searchBarTest() {
        Application application = compile("net/n2oapp/framework/config/metadata/menu/pageWithoutLabel.page.xml",
                "net/n2oapp/framework/config/metadata/application/testPage.page.xml",
                "net/n2oapp/framework/config/metadata/application/headerWithSearch.application.xml",
                "net/n2oapp/framework/config/metadata/application/search.query.xml")
                .bind().get(new ApplicationContext("headerWithSearch"), null);
        SearchBar searchBar = application.getHeader().getSearch();
        assertThat(searchBar, notNullValue());
        assertThat("urlId", is(searchBar.getUrlFieldId()));
        assertThat("labelId", is(searchBar.getLabelFieldId()));
        assertThat("iconId", is(searchBar.getIconFieldId()));
        assertThat("descriptionId", is(searchBar.getDescrFieldId()));

        assertThat(searchBar.getSearchPageLocation(), notNullValue());
        assertThat(SearchBar.LinkTypeEnum.INNER, is(searchBar.getSearchPageLocation().getLinkType()));

        assertThat(searchBar.getDataProvider(), notNullValue());
        assertThat("n2o/data/search", is(searchBar.getDataProvider().getUrl()));
        assertThat("filterId", is(searchBar.getDataProvider().getQuickSearchParam()));
    }

    @Test
    void simple() {
        Application application = compile("net/n2oapp/framework/config/metadata/menu/pageWithoutLabel.page.xml",
                "net/n2oapp/framework/config/metadata/application/headerSimpleTest.application.xml")
                .bind().get(new ApplicationContext("headerSimpleTest"), null);
        Header header = application.getHeader();
        assertThat(header.getSrc(), is("test"));
        assertThat(header.getClassName(), is("class"));
        assertThat(header.getLogo().getTitle(), is("`name`"));
        assertThat(header.getLogo().getHref(), is("/pageRoute"));
        assertThat(header.getStyle().size(), is(1));
        assertThat(header.getStyle().get("marginLeft"), is("10px"));
        assertThat(header.getDatasource(), is("test"));
    }
}
