package net.n2oapp.framework.sandbox.autotest.cases;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
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

public class Case43AT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("cases/7.1/case43/index.page.xml"),
                new CompileInfo("cases/7.1/case43/test.page.xml"),
                new CompileInfo("META-INF/conf/test.object.xml"),
                new CompileInfo("META-INF/conf/test.query.xml"));
    }

    @Test
    public void pageTitlesTest() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Разрешение ссылок в названии страницы");

        RegionItems content = page.regions().region(0, SimpleRegion.class).content();
        Button breadButton = content.widget(FormWidget.class).toolbar().topLeft().button("Хлебные крошки");
        breadButton.shouldExists();
        Button modalButton = content.widget(FormWidget.class).toolbar().topLeft().button("Модальное окно");
        modalButton.shouldExists();
        Button breadButton2 = content.widget(1, FormWidget.class).toolbar().bottomLeft().button("Хлебные крошки от master");
        breadButton2.shouldExists();
        Button modalButton2 = content.widget(1, FormWidget.class).toolbar().bottomLeft().button("Модальное окно от detail");
        modalButton2.shouldExists();

        TableWidget table = content.widget(TableWidget.class);
        table.shouldExists();
        table.columns().headers().shouldHaveSize(3);
        table.columns().rows().shouldHaveSize(4);
        TableWidget.Rows rows = table.columns().rows();

        rows.row(1).click();
        rows.shouldBeSelected(1);
        breadButton.click();
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Разрешение ссылок в названии страницы");
        page.breadcrumb().crumb(1).shouldHaveLabel("Страница name=test2 type=1");
        page.breadcrumb().crumb(0).click();
        page.breadcrumb().shouldHaveSize(1);
        page.breadcrumb().crumb(0).shouldHaveLabel("Разрешение ссылок в названии страницы");

        rows.row(3).click();
        rows.shouldBeSelected(3);
        modalButton.click();
        Modal modal = N2oSelenide.modal();
        modal.shouldExists();
        modal.shouldHaveTitle("Страница name=test4 type=2");
        modal.close();

        rows.row(2).click();
        rows.shouldBeSelected(2);
        breadButton2.click();
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Разрешение ссылок в названии страницы");
        page.breadcrumb().crumb(1).shouldHaveLabel("Страница name=test3 type=2");
        page.breadcrumb().crumb(0).click();
        page.breadcrumb().shouldHaveSize(1);
        page.breadcrumb().crumb(0).shouldHaveLabel("Разрешение ссылок в названии страницы");

        rows.row(0).click();
        rows.shouldBeSelected(0);
        modalButton2.click();
        modal = N2oSelenide.modal();
        modal.shouldExists();
        modal.shouldHaveTitle("Страница name=test1 type=1");
        modal.close();
        page.shouldExists();
    }
}