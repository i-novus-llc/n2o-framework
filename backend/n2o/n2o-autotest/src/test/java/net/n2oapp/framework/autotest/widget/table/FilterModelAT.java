package net.n2oapp.framework.autotest.widget.table;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.MaskedInput;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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

import java.time.Duration;

/**
 * Автотест для фильтр-модели таблциы
 */
public class FilterModelAT extends AutoTestBase {

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
        setJsonPath("net/n2oapp/framework/autotest/widget/table/filter_model");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filter_model/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filter_model/searchPatient.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filter_model/test.query.xml"));
    }

    @Test
    void test() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        MaskedInput snils = table.filters().fields().field("СНИЛС").control(MaskedInput.class);
        InputText name = table.filters().fields().field("Имя").control(InputText.class);

        name.shouldBeEmpty();
        snils.setValue("324-234-324 32");
        table.filters().fields().field(Condition.cssClass("n2o-button-field"), ButtonField.class).click();
        name.shouldHaveValue("Роман", Duration.ofSeconds(10));
    }
}
