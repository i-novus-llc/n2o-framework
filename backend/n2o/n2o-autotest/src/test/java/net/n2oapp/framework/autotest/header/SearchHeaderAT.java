package net.n2oapp.framework.autotest.header;

import net.n2oapp.framework.autotest.api.collection.Search;
import net.n2oapp.framework.autotest.api.component.header.SearchItem;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.run.AutoTestApplication;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Автотест поиска в хедере
 */
@SpringBootTest(properties = {"n2o.engine.test.classpath=/net/n2oapp/framework/autotest/search_bar"},
        classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchHeaderAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/search_bar/popupSearch.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/search_bar/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/search_bar/search.header.xml"));
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oAllDataPack());
    }

    @Test
    public void searchHeaderTest() {
        String rootUrl = getBaseUrl();
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        Search searchResult = page.header().search();

        searchResult.shouldHaveSize(4);
        searchResult.item(0, SearchItem.class).shouldHaveTitle("label1");
        searchResult.item(0, SearchItem.class).shouldHaveDescription("description1");
        searchResult.item(0, SearchItem.class).shouldHaveLink(rootUrl + "/href1");
        searchResult.item(0, SearchItem.class).shouldHaveIcon("icon1");

        searchResult.item(1, SearchItem.class).shouldHaveTitle("label2");
        searchResult.item(1, SearchItem.class).shouldHaveDescription("description2");
        searchResult.item(1, SearchItem.class).shouldHaveLink(rootUrl + "/href2");
        searchResult.item(1, SearchItem.class).shouldHaveIcon("icon2");

        searchResult.item(2, SearchItem.class).shouldHaveTitle("label3");
        searchResult.item(2, SearchItem.class).shouldHaveDescription("description3");
        searchResult.item(2, SearchItem.class).shouldHaveLink(rootUrl + "/href3");
        searchResult.item(2, SearchItem.class).shouldHaveIcon("icon3");

        searchResult.item(3, SearchItem.class).shouldHaveTitle("label4");
        searchResult.item(3, SearchItem.class).shouldHaveDescription("description4");
        searchResult.item(3, SearchItem.class).shouldHaveLink(rootUrl + "/href4");
        searchResult.item(3, SearchItem.class).shouldHaveIcon("icon4");


        searchResult = page.header().search("filterValue2");
        searchResult.shouldHaveSize(1);
        searchResult.item(0, SearchItem.class).shouldHaveTitle("label2");
        searchResult.item(0, SearchItem.class).shouldHaveDescription("description2");
        searchResult.item(0, SearchItem.class).shouldHaveLink(rootUrl + "/href2");
        searchResult.item(0, SearchItem.class).shouldHaveIcon("icon2");
    }

}