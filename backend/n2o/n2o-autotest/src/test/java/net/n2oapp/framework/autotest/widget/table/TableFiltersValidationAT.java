package net.n2oapp.framework.autotest.widget.table;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Автотест по проверке работы валидации у фильтров и запроса данных при их ошибке
 */

public class TableFiltersValidationAT extends AutoTestBase {
    private final N2oController controllerMock = Mockito.mock(N2oController.class);

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
        Configuration.headless=false;

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        when(controllerMock.getData(any())).thenReturn(null);
        Assertions.assertNull(verify(controllerMock, never()).getData(any()));

        TableWidget tableWidget = page.widget(TableWidget.class);
        tableWidget.filters().toolbar().button("Найти").click();
        Assertions.assertNull(verify(controllerMock, never()).getData(any()));
        StandardField field = tableWidget.filters().fields().field("name");
        field.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
        InputText inputText = field.control(InputText.class);

        inputText.val("test1");
        tableWidget.filters().toolbar().button("Найти").click();
        Assertions.assertNull(verify(controllerMock, never()).getData(any()));
        tableWidget.columns().rows().row(0).cell(0).textShouldHave("test1");
    }
}
