package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.control.Progress;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для компонента отображения прогресса
 */
public class ProgressAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/progress/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/progress/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));

        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oAllDataPack());
    }

    @Test
    public void testProgress() {
        Progress progress = page.widget(FormWidget.class).fields().field("Progress")
                .control(Progress.class);
        progress.shouldExists();

        progress.shouldHaveText("Text");
        progress.shouldHaveValue("17");
        progress.shouldHaveMax("25");
        progress.shouldBeAnimated();
        progress.shouldBeStriped();
        progress.shouldHaveColor(Colors.SUCCESS);
    }
}
