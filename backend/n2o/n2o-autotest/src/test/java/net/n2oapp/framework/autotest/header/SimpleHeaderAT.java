package net.n2oapp.framework.autotest.header;

import net.n2oapp.framework.autotest.ComponentFactory;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Menu;
import net.n2oapp.framework.autotest.api.component.header.Dropdown;
import net.n2oapp.framework.autotest.api.component.header.Link;
import net.n2oapp.framework.autotest.impl.collection.N2oRegions;
import net.n2oapp.framework.autotest.impl.collection.N2oWidgets;
import net.n2oapp.framework.autotest.impl.component.header.N2oDropdown;
import net.n2oapp.framework.autotest.impl.component.header.N2oLink;
import net.n2oapp.framework.autotest.impl.component.page.N2oSimplePage;
import net.n2oapp.framework.autotest.test.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотест хедера
 */
public class SimpleHeaderAT extends AutoTestBase {

    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
        N2oSelenide.setFactory(new ComponentFactory().addCollections(N2oRegions.class, N2oWidgets.class));
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack());
    }

    @Test
    public void simpleHeader() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/header/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/header/testPage1.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/header/testJsonSimpleHeader.header.xml"));

        String rootUrl = getRootUrl();
        N2oSimplePage page = openRoot(N2oSimplePage.class);

        page.shouldExists();
        page.single();
        page.header().shouldBrandName("Лого");
        Menu menu = page.header().menu();
        menu.shouldHaveSize(2);
        Link link = page.header().menu().item(0, N2oLink.class);
        link.shouldHaveLabel("ссылка");
        link.shouldHaveUrl(rootUrl + "/");

        Dropdown dropdown = page.header().menu().item(1, N2oDropdown.class);
        dropdown.shouldHaveLabel("список");
        dropdown.click();
        dropdown.item(0).shouldHaveLabel("Название страницы");
        dropdown.item(0).shouldHaveUrl(rootUrl + "/#/pageRoute");
        dropdown.item(1).shouldHaveLabel("элемент списка №2");
        dropdown.item(1).shouldHaveUrl(rootUrl + "/#/pageRoute1");

        menu = page.header().extra();
        menu.shouldHaveSize(1);
        link = menu.item(0, N2oLink.class);
        link.shouldHaveLabel("ссылка из extra-menu");
        link.shouldHaveUrl(getRootUrl() + "/");
    }
}
