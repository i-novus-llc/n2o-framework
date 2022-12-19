package net.n2oapp.framework.autotest.markdown;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.Markdown;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для проверки компонента markdown
 */

public class MarkdownAT extends AutoTestBase {
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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/markdown/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/markdown/page.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/markdown/test.query.xml"));
    }

    @Test
    public void test() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget formWidget = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        formWidget.shouldExists();

        Markdown markdown = formWidget.fields().markdown();
        markdown.shouldExists();

        StandardButton markdownBtn = markdown.markdownBtn("n2o-button");
        markdownBtn.shouldExists();
        markdownBtn.shouldBeVisible();
        markdownBtn.shouldBeEnabled();
        markdownBtn.click();

        StandardPage page1 = N2oSelenide.page(StandardPage.class);
        page1.shouldExists();
        page1.titleShouldHaveText("page");
    }
}
