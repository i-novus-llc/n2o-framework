package net.n2oapp.framework.autotest.condition.column.dependency.visibility;

import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для проверки зависимости visibility у колонок таблицы
 */
public class ColumnVisibilityAT extends AutoTestBase {

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
                new N2oAllPack()
        );
    }
    
    @Test
    void dynamicVisibility() {
        setJsonPath("net/n2oapp/framework/autotest/condition/column/dependency/visibility");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/column/dependency/visibility/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/column/dependency/visibility/test.query.xml")
        );

        final StandardPage page = open(StandardPage.class);
        page.shouldExists();

        final SimpleRegion region = page.regions().region(0, SimpleRegion.class);

        final FormWidget form = region.content().widget(0, FormWidget.class);
        form.shouldExists();
        final TableWidget table = region.content().widget(1, TableWidget.class);
        table.shouldExists();

        RadioGroup radio = form.fieldsets()
                .fieldset(0, SimpleFieldSet.class)
                .fields()
                .field("radio")
                .control(RadioGroup.class);
        radio.shouldBeChecked("visible");
        
        table.columns().headers().shouldHaveSize(2);
        
        radio.check("not_visible");

        table.columns().headers().shouldHaveSize(1);

        radio.check("visible");

        table.columns().headers().shouldHaveSize(2);
    }
}
