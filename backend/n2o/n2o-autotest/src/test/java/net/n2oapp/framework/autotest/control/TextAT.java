package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.snippet.Text;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для компонента текста
 */
public class TextAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testText() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/text/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Text text = page.widget(FormWidget.class).fields().field(Text.class);
        text.shouldExists();

        text.shouldHaveText("Hello, World!");
    }
}
