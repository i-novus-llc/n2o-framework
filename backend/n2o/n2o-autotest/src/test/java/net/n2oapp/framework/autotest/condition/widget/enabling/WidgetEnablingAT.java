package net.n2oapp.framework.autotest.condition.widget.enabling;

import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.Checkbox;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
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
public class WidgetEnablingAT extends AutoTestBase {

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
        setJsonPath("net/n2oapp/framework/autotest/condition/widget/dependency/enabling");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/widget/dependency/enabling/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/widget/dependency/enabling/test.query.xml")
        );
    }

    @Test
    public void test() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget form = page.regions()
                .region(0, N2oSimpleRegion.class)
                .content()
                .widget(0, FormWidget.class);
        TableWidget table = page.regions()
                .region(0, N2oSimpleRegion.class)
                .content()
                .widget(1, TableWidget.class);
        table.columns().rows().shouldHaveSize(4);

        StandardButton button = table.toolbar().topLeft().button("test");
        button.shouldBeEnabled();
        button.click();
        page.alerts(Alert.Placement.top).alert(0).shouldExists();
        page.alerts(Alert.Placement.top).alert(0).shouldNotExists();

        Cells row = table.columns().rows().row(2);
        row.shouldBeClickable();
        row.click();
        page.alerts(Alert.Placement.top).alert(0).shouldExists();

        Checkbox checkbox = form.fieldsets()
                .fieldset(0, SimpleFieldSet.class)
                .fields()
                .field("enabled")
                .control(Checkbox.class);
        checkbox.setChecked(false);

        table.shouldBeDisabled();

        checkbox.setChecked(true);

        table.shouldBeEnabled();
    }
}
