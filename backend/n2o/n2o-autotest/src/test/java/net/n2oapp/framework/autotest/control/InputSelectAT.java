package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.component.control.N2oInputSelect;
import net.n2oapp.framework.autotest.impl.component.page.N2oSimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотест поля ввода текста с выбором из выпадающего списка (input-select)
 */
public class InputSelectAT extends AutoTestBase {

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
    public void testSingle() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/input_select/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        N2oSimplePage page = open(N2oSimplePage.class);
        page.shouldExists();

        N2oInputSelect input = page.single().widget(FormWidget.class).fields().field("InputSelect1")
                .control(N2oInputSelect.class);
        input.shouldExists();

        input.shouldSelected("");
        input.select(1);
        input.shouldSelected("Two");
        input.clear();
        input.shouldSelected("");

        input.val("Three");
        input.shouldHaveValue("Three");
    }

    @Test
    public void testMulti() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/input_select/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        N2oSimplePage page = open(N2oSimplePage.class);
        page.shouldExists();

        N2oInputSelect input = page.single().widget(FormWidget.class).fields().field("InputSelect2")
                .control(N2oInputSelect.class);
        input.shouldExists();

        String[] empty = new String[0];
        input.shouldSelectedMulti(empty);
        input.selectMulti(1, 2);
        input.shouldSelectedMulti("Two", "Three");
        input.clear();
        input.shouldSelectedMulti(empty);

        input.selectMulti(2, 1, 0);
        input.shouldSelectedMulti("Three", "Two", "One");

        // удаление выбранных значений
        input.clearItems("Three");
        input.shouldSelectedMulti("Two", "One");
        input.clearItems("Two", "One");
        input.shouldSelectedMulti(empty);
    }

    @Test
    public void testCheckboxes() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/input_select/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        N2oSimplePage page = open(N2oSimplePage.class);
        page.shouldExists();

        N2oInputSelect input = page.single().widget(FormWidget.class).fields().field("InputSelect3")
                .control(N2oInputSelect.class);
        input.shouldExists();

        String[] empty = new String[0];
        input.shouldSelectedMulti(empty);
        input.selectMulti(1, 2);
        input.shouldSelectedMulti("Two", "Three");
        input.clear();
        input.shouldSelectedMulti(empty);

        input.selectMulti(2, 1, 0);
        input.shouldSelectedMulti("Three", "Two", "One");

        // удаление выбранных значений
        input.clearItems("Three");
        input.shouldSelectedMulti("Two", "One");
        input.clearItems("Two", "One");
        input.shouldSelectedMulti(empty);
    }
}
