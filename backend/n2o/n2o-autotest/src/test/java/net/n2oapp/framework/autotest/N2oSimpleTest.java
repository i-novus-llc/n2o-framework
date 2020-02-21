package net.n2oapp.framework.autotest;

import net.n2oapp.framework.autotest.impl.collection.N2oRegions;
import net.n2oapp.framework.autotest.impl.collection.N2oWidgets;
import net.n2oapp.framework.autotest.impl.component.page.N2oSimplePage;
import net.n2oapp.framework.autotest.impl.component.widget.N2oFormWidget;
import net.n2oapp.framework.autotest.test.AutoTestApplication;
import net.n2oapp.framework.autotest.test.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class N2oSimpleTest extends AutoTestBase {

    @LocalServerPort
    private int port;

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
        builder.packs(new N2oHeaderPack(), new N2oPagesPack(), new N2oWidgetsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/header/test.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/header/index.page.xml"));
    }

    @Test
    public void test() {
        N2oSimplePage page = N2oSelenide.open("http://localhost:" + port, N2oSimplePage.class);
        page.shouldExists();
        page.single().shouldHaveSize(1);
        page.single().widget(N2oFormWidget.class).shouldExists();
    }
}
