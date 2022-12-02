package net.n2oapp.framework.autotest.widget.table;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.autotest.run.N2oController;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Автотест по проверке работы валидации у фильтров и запроса данных при их ошибке
 */

public class TableFiltersValidationAT extends AutoTestBase {

    @SpyBean
    private N2oController controller;

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters_validation/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters_validation/test.query.xml"));
    }

    @Test
    public void test() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget tableWidget = page.widget(TableWidget.class);
        tableWidget.filters().toolbar().button("Найти").click();
        StandardField field = tableWidget.filters().fields().field("name");
        field.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
        verifyNeverGetDataInvocation("Запрос за данными таблицы при валидации фильтров");

        InputText inputText = field.control(InputText.class);
        inputText.val("test1");
        tableWidget.filters().toolbar().button("Найти").click();
        tableWidget.columns().rows().row(0).cell(0).textShouldHave("test1");
    }

    private void verifyNeverGetDataInvocation(String errorMessage) {
        try {
            verify(controller, never()).getData(any());
        } catch (NeverWantedButInvoked e) {
            throw new AssertionError(errorMessage);
        }
    }
}
