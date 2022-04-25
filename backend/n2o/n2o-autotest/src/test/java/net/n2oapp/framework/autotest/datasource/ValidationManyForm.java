package net.n2oapp.framework.autotest.datasource;

import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.DateInput;
import net.n2oapp.framework.autotest.api.component.control.InputText;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ValidationManyForm extends AutoTestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    @Test
    public void testValidationManyForm(){
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasource/validationManyForm/index.page.xml"),
                        new CompileInfo("net/n2oapp/framework/autotest/datasource/validationManyForm/test.query.xml"),
                        new CompileInfo("net/n2oapp/framework/autotest/datasource/validationManyForm/test.object.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        InputText nameInput = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class)
                                    .fields().field("Наименование").control(InputText.class);
        DateInput birthdayInput = page.regions().region(0, SimpleRegion.class).content().widget(2, FormWidget.class)
                .fields().field("Дата рождения").control(DateInput.class);
        Button createButton = page.toolbar().topLeft().button("Создать");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        nameInput.val("Сергей");
        birthdayInput.val(formatter.format(LocalDate.now().plusDays(1)));
        createButton.click();
        page.alerts().alert(0).shouldHaveText("Дата рождения не может быть в будущем");
        table.columns().rows().shouldNotHaveRows();

        nameInput.val("Сергей");
        birthdayInput.val(formatter.format(LocalDate.now().minusDays(1)));
        createButton.click();
        page.alerts().alert(0).shouldHaveText("Данные сохранены");
        table.columns().rows().shouldHaveSize(1);

        nameInput.val("Сергей");
        birthdayInput.val(formatter.format(LocalDate.now().minusDays(1)));
        createButton.click();
        page.alerts().alert(0).shouldHaveText("Имя Сергей уже существует");
        table.columns().rows().shouldHaveSize(1);

        nameInput.val("Артем");
        birthdayInput.val(formatter.format(LocalDate.now().minusDays(1)));
        createButton.click();
        page.alerts().alert(0).shouldHaveText("Данные сохранены");
        table.columns().rows().shouldHaveSize(2);



    }
}
