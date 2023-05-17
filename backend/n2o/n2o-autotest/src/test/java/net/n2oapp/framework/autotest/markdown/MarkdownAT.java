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
        builder.packs(
                new N2oApplicationPack(),
                new N2oAllPagesPack(),
                new N2oAllDataPack()
        );
    }

    @Test
    public void buttonCompile() {
        setJsonPath("net/n2oapp/framework/autotest/markdown/button");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/markdown/button/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/markdown/button/page.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/markdown/button/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget formWidget = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        formWidget.shouldExists();

        Markdown markdown = formWidget.fields().field(0, Markdown.class);
        markdown.shouldExists();

        StandardButton markdownBtn = markdown.button("n2o-button");
        markdownBtn.shouldExists();
        markdownBtn.shouldBeVisible();
        markdownBtn.shouldBeEnabled();
        markdownBtn.click();

        StandardPage page1 = N2oSelenide.page(StandardPage.class);
        page1.shouldExists();
        page1.shouldHaveTitle("page");
    }

    @Test
    public void wordwrapAndReadFromData() {
        setJsonPath("net/n2oapp/framework/autotest/markdown/wordwrap_and_read_from_data");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/markdown/wordwrap_and_read_from_data/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/markdown/wordwrap_and_read_from_data/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget formWidget = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        formWidget.shouldExists();

        Markdown markdown = formWidget.fields().field(0, Markdown.class);
        markdown.shouldExists();
        markdown.shouldHaveText("aaa\nbbb\nccc\n" +
                "Жирный\nМоноширный\n" +
                "Курсив Моноширный");
        markdown.shouldHaveElement("p > strong + br + code");
        markdown.shouldHaveElement("p > em + br + code");
    }
}
