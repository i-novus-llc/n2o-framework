package net.n2oapp.framework.sandbox.autotest.cases;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.DateInput;
import net.n2oapp.framework.autotest.api.component.control.DateInterval;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestApplication;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"server.servlet.context-path=/"},
        classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AutoFocusDatePopUpAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("cases/7.17/date_popup/index.page.xml"),
                new CompileInfo("cases/7.17/date_popup/date_time.page.xml"),
                new CompileInfo("cases/7.17/date_popup/date_interval.page.xml"));
    }

    @Test
    public void popUpTest() {
        SimplePage indexPage = open(SimplePage.class);
        indexPage.shouldExists();
        indexPage.breadcrumb().crumb(0).shouldHaveLabel("При автофокусе на date-time и date-interval открывается pop-up");

        StandardButton dateTimeButton = indexPage.widget(FormWidget.class)
                .toolbar()
                .topLeft()
                .button(0, StandardButton.class);
        dateTimeButton.shouldExists();
        dateTimeButton.shouldHaveLabel("Открыть форму с date-time");
        dateTimeButton.click();
        Modal modal = N2oSelenide.modal();
        modal.shouldExists();
        modal.shouldHaveTitle("Проверка открытия pop-up при автофокусе на date-time");
        DateInput dateInput = modal.content(SimplePage.class)
                .widget(FormWidget.class)
                .fields()
                .field("Дата")
                .control(DateInput.class);
        dateInput.shouldExists();
        dateInput.shouldBeClosed();
        dateInput.openPopup();
        dateInput.shouldBeOpened();
        modal.close();

        StandardButton dateIntervalButton = indexPage.widget(FormWidget.class)
                .toolbar()
                .topLeft()
                .button(1, StandardButton.class);
        dateIntervalButton.shouldExists();
        dateIntervalButton.shouldHaveLabel("Открыть форму с date-interval");
        dateIntervalButton.click();
        modal = N2oSelenide.modal();
        modal.shouldExists();
        modal.shouldHaveTitle("Проверка открытия pop-up при автофокусе на date-interval");
        DateInterval dateInterval = modal.content(SimplePage.class)
                .widget(FormWidget.class)
                .fields()
                .field("Дата")
                .control(DateInterval.class);
        dateInterval.shouldExists();
        dateInterval.shouldBeClosed();
        dateInterval.openPopup();
        dateInterval.shouldBeOpened();
        modal.close();
    }
}
