package net.n2oapp.framework.config.metadata.application;

import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.header.Header;
import net.n2oapp.framework.api.metadata.header.HeaderItem;
import net.n2oapp.framework.api.metadata.header.SearchBar;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции заголовка
 */
public class HeaderCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
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
    public void inlineMenu() {
        Application application = compile("net/n2oapp/framework/config/metadata/menu/pageWithoutLabel.page.xml",
                "net/n2oapp/framework/config/metadata/application/testPage.page.xml",
                "net/n2oapp/framework/config/metadata/application/headerWithMenu.application.xml")
                .get(new ApplicationContext("headerWithMenu"));

        Header header = application.getHeader();
        assertThat(header.getLogo().getTitle(), is("N2O"));
        assertThat(header.getSrc(), is("test"));
        assertThat(header.getClassName(), is("class"));
        assertThat(header.getLogo().getHref(), is("/pageRoute"));
        assertThat(header.getStyle().size(), is(1));
        assertThat(header.getStyle().get("marginLeft"),is("10px"));

        assertThat(header.getMenu().getItems().size(), is(3));
        List<HeaderItem> headerItems = header.getMenu().getItems();
        // sub-menu
        assertThat(headerItems.get(0).getTitle(), is("test"));
        assertThat(headerItems.get(0).getSubItems().size(), is(2));
        assertThat(headerItems.get(0).getType(), is("dropdown"));
        // page
        assertThat(headerItems.get(1).getTitle(), is("headerLabel"));
        assertThat(headerItems.get(1).getHref(), is("/mi3"));
        assertThat(headerItems.get(1).getPageId(), is("pageWithoutLabel"));
        assertThat(headerItems.get(1).getLinkType(), is(HeaderItem.LinkType.inner));
        assertThat(headerItems.get(1).getSubItems(), nullValue());
        assertThat(headerItems.get(1).getType(), is("link"));
        // a
        assertThat(headerItems.get(2).getTitle(), is("hrefLabel"));
        assertThat(headerItems.get(2).getHref(), is("http://test.com"));
        assertThat(headerItems.get(2).getLinkType(), is(HeaderItem.LinkType.outer));
        assertThat(headerItems.get(2).getSubItems(), nullValue());
        assertThat(headerItems.get(2).getType(), is("link"));

        ArrayList<HeaderItem> subItems = headerItems.get(0).getSubItems();
        // sub-menu page
        assertThat(subItems.get(0).getTitle(), is("test2"));
        assertThat(subItems.get(0).getHref(), is("/page1"));
        assertThat(subItems.get(0).getPageId(), is("pageWithoutLabel"));
        assertThat(subItems.get(0).getLinkType(), is(HeaderItem.LinkType.inner));
        assertThat(subItems.get(0).getSubItems(), nullValue());
        assertThat(subItems.get(0).getType(), is("link"));
        assertThat(subItems.get(0).getProperties().size(), is(1));
        assertThat(subItems.get(0).getProperties().get("testAttr"), is("testAttribute"));
        // sub-menu a
        assertThat(subItems.get(1).getTitle(), is("hrefLabel"));
        assertThat(subItems.get(1).getHref(), is("http://test.com"));
        assertThat(subItems.get(1).getLinkType(), is(HeaderItem.LinkType.outer));
        assertThat(subItems.get(1).getSubItems(), nullValue());
        assertThat(subItems.get(1).getType(), is("link"));
        assertThat(header.getExtraMenu().getItems().size(), is(1));
        HeaderItem extraItem = header.getExtraMenu().getItems().get(0);
        // sub-menu
        assertThat(extraItem.getTitle(), is("#{username}"));
        assertThat(extraItem.getSubItems().size(), is(1));
        assertThat(extraItem.getType(), is("dropdown"));
        subItems = extraItem.getSubItems();
        // sub-menu a
        assertThat(subItems.get(0).getTitle(), is("Test"));
        assertThat(subItems.get(0).getHref(), is("https://ya.ru/"));
        assertThat(subItems.get(0).getLinkType(), is(HeaderItem.LinkType.outer));
        assertThat(subItems.get(0).getIcon(), is("test-icon"));
        assertThat(subItems.get(0).getSubItems(), nullValue());
        assertThat(subItems.get(0).getType(), is("link"));
    }

    @Test
    public void externalMenu() {
        Application application = compile("net/n2oapp/framework/config/metadata/menu/pageWithoutLabel.page.xml",
                "net/n2oapp/framework/config/metadata/application/headerWithExternalMenu.application.xml",
                "net/n2oapp/framework/config/metadata/application/testPage.page.xml",
                "net/n2oapp/framework/config/metadata/application/testMenu.menu.xml")
                .get(new ApplicationContext("headerWithExternalMenu"));

        Header header = application.getHeader();
        assertThat(header.getLogo().getHref(), is("http://google.com/"));
        assertThat(header.getMenu().getItems().size(), is(3));
        assertThat(header.getMenu().getItems().get(0).getSubItems().size(), is(2));
        assertThat(header.getMenu().getItems().get(0).getSubItems().get(0).getTitle(), is("test2"));
        assertThat(header.getMenu().getItems().get(0).getSubItems().get(0).getProperties().get("testAttr"), is("testAttribute"));
        assertThat(header.getMenu().getItems().get(0).getSubItems().get(0).getJsonProperties().get("testAttr"), is("testAttribute"));
        assertThat(header.getMenu().getItems().get(1).getTitle(), is("headerLabel"));
    }

    @Test
    public void searchBarTest() {
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
        assertThat("advancedUrl", is(searchBar.getSearchPageLocation().getUrl()));
        assertThat("param", is(searchBar.getSearchPageLocation().getSearchQueryName()));
        assertThat(SearchBar.LinkType.inner, is(searchBar.getSearchPageLocation().getLinkType()));

        assertThat(searchBar.getDataProvider(), notNullValue());
        assertThat("n2o/data/search", is(searchBar.getDataProvider().getUrl()));
        assertThat("filterId", is(searchBar.getDataProvider().getQuickSearchParam()));
    }
}
