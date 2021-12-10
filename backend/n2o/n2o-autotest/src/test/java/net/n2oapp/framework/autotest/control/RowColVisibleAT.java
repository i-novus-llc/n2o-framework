package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Text;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирование видимости полей внутри столбцов
 */
public class RowColVisibleAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/visible/col_row/index.page.xml"));
    }

    @Test
    public void visibilityControl() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        RadioGroup type = fields.field("type").control(RadioGroup.class);
        type.shouldExists();
        type.check("visible");

        Text field = fields.field(1, Text.class);
        field.shouldExists();
        field.shouldHaveText("Текстовое поле 1");

        field = fields.field(2, Text.class);
        field.shouldExists();
        field.shouldHaveText("Текстовое поле 2");

        field = fields.field(3, Text.class);
        field.shouldExists();
        field.shouldHaveText("Текстовое поле 3");

        field = fields.field(4, Text.class);
        field.shouldExists();
        field.shouldHaveText("Текстовое поле 4");

        type.check("not visible");

        field = fields.field(1, Text.class);
        field.shouldNotExists();

        field = fields.field(2, Text.class);
        field.shouldNotExists();

        field = fields.field(3, Text.class);
        field.shouldNotExists();

        field = fields.field(4, Text.class);
        field.shouldNotExists();
    }
}
