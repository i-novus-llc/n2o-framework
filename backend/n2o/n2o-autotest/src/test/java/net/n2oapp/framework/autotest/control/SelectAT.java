package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.component.control.N2oSelect;
import net.n2oapp.framework.autotest.impl.component.page.N2oSimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотест поля для выбора из выпадающего списка
 */
public class SelectAT extends AutoTestBase {

    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testSelect() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/select/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        N2oSimplePage page = open(N2oSimplePage.class);
        page.shouldExists();

        N2oSelect input = page.single().widget(FormWidget.class).fields().field("Select1")
                .control(N2oSelect.class);
        input.shouldExists();

        input.shouldSelected("");
        input.select(1);
        input.shouldSelected("Two");
        input.shouldBeClearable();
        input.clear();
        input.shouldSelected("");

        input = page.single().widget(FormWidget.class).fields().field("Select2")
                .control(N2oSelect.class);
        input.shouldExists();

        input.shouldNotBeClearable();
    }
}
