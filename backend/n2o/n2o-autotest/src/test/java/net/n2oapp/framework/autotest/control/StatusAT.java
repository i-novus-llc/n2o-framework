package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Status;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для компонента отображения статуса
 */
class StatusAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/status/index.page.xml"));

    }

    @Test
    void testStatus() {
        Status status1 = page.widget(FormWidget.class).fields().field(Status.class);
        status1.shouldExists();
        status1.shouldHaveText("Заявка принята");
        status1.shouldHaveTextPosition(PositionEnum.RIGHT);
        status1.shouldHaveColor(ColorsEnum.SUCCESS);

        Status status2 = page.widget(FormWidget.class).fields().field(1, Status.class);
        status2.shouldExists();
        status2.shouldHaveText("Заявка отклонена");
        status2.shouldHaveTextPosition(PositionEnum.LEFT);
        status2.shouldHaveColor(ColorsEnum.DANGER);
    }
}
