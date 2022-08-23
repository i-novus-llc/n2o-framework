package net.n2oapp.framework.autotest.widget.expanded_widget;

import net.n2oapp.framework.autotest.api.component.control.Checkbox;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.impl.component.region.N2oSimpleRegion;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для проверки скрытия виджета по условию
 */

public class ExpandedWidgetAT extends AutoTestBase {
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
        builder.packs(new N2oApplicationPack(), new N2oAllPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/simple/test.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/expanded_widget/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/expanded_widget/test.query.xml"),
        new CompileInfo("net/n2oapp/framework/autotest/widget/expanded_widget/test2.query.xml"));
    }

    @Test
    public void test() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.shouldBeVisible();

        FormWidget formWidget = page.regions().region(0, N2oSimpleRegion.class).content().widget(0, FormWidget.class);
        Checkbox exists = formWidget.fieldsets().fieldset(0, SimpleFieldSet.class).fields().field("exists").control(Checkbox.class);
        exists.shouldBeChecked();

        TableWidget tableWidget = page.regions().region(0, N2oSimpleRegion.class).content().widget(1, TableWidget.class);
        tableWidget.shouldExists();
        tableWidget.shouldBeVisible();

        exists.setChecked(false);
        tableWidget.shouldNotBeVisible();

        exists.setChecked(true);
        tableWidget.shouldExists();
        tableWidget.shouldBeVisible();
    }
}
