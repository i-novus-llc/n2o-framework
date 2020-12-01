package net.n2oapp.framework.autotest.fieldset;

import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.component.fieldset.LineFieldSet;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для филдсета с горизонтальным делителем
 */
public class LineFieldSetAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/list/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(),
                new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testLineFieldSet() {
        FieldSets fieldsets = page.widget(FormWidget.class).fieldsets();
        // empty fieldset
        fieldsets.fieldset(LineFieldSet.class).shouldBeEmpty();

        // expanded
        LineFieldSet fieldset = fieldsets.fieldset(1, LineFieldSet.class);
        fieldset.fields().shouldHaveSize(2);
        fieldset.shouldBeCollapsible();
        fieldset.shouldBeExpanded();
        fieldset.shouldHaveLabel("Line2");
        fieldset.collapseContent();
        fieldset.shouldBeCollapsed();
        fieldset.expandContent();
        fieldset.shouldBeExpanded();

        // collapsed
        fieldset = fieldsets.fieldset(2, LineFieldSet.class);
        fieldset.shouldBeCollapsed();
        fieldset.shouldBeCollapsible();
        fieldset.shouldHaveLabel("Line3");

        // not collapsible fieldset
        fieldset = fieldsets.fieldset(3, LineFieldSet.class);
        fieldset.shouldNotBeCollapsible();
        fieldset.shouldHaveLabel("Line4");
    }
}
