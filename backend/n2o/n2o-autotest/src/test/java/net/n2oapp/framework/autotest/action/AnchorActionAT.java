package net.n2oapp.framework.autotest.action;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
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

/**
 * Автотест для действия перехода по ссылке
 */
public class AnchorActionAT extends AutoTestBase {

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
    }


    @Test
    public void testAnchorAction() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/anchor/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/anchor/test.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/anchor/test.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тестирование действия перехода по ссылке");

        TableWidget table = page.widget(TableWidget.class);
        table.columns().rows().row(2).click();
        table.toolbar().topLeft().button("Открыть").click();

        SimplePage open = N2oSelenide.page(SimplePage.class);
        page.breadcrumb().crumb(1).shouldHaveLabel("Вторая страница");
        open.toolbar().bottomRight().button("Ссылка").click();

        open.shouldHaveUrlLike(getBaseUrl() + "/link/3/");
    }


    /**
     *Тест проверяющий резолв атрибута href
     */
    @Test
    public void testHrefResolve() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/anchor/check_href/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/anchor/check_href/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget form = page
                .regions()
                .region(0, SimpleRegion.class)
                .content()
                .widget(FormWidget.class);
        form.shouldExists();

        ButtonField formBtn = form.fields().field("Кнопка тулбара формы", ButtonField.class);
        formBtn.shouldExists();
        formBtn.shouldBeEnabled();
        formBtn.click();

        page.shouldHaveUrlLike("https://example.com/");

        Selenide.back();

        StandardButton pageBtn = page.toolbar().topLeft().button("Кнопка тулбара страницы");
        pageBtn.shouldExists();
        pageBtn.shouldBeEnabled();
        pageBtn.click();

        page.shouldHaveUrlLike("https://example.com/");
    }
}
