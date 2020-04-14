package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.control.Select;
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
 * Автотест компонента выбора из выпадающего списка
 */
public class SelectAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/select/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testSelect() {
        Select input = page.single().widget(FormWidget.class).fields().field("Select1")
                .control(Select.class);
        input.shouldExists();

        input.shouldBeEmpty();
        input.select(1);
        input.shouldSelected("Two");
        input.shouldBeCleanable();
        input.clear();
        input.shouldBeEmpty();

        input = page.single().widget(FormWidget.class).fields().field("Select2")
                .control(Select.class);
        input.shouldExists();

        input.select(1);
        input.shouldNotBeCleanable();
    }

    @Test
    public void testCheckboxesType() {
        Select input = page.single().widget(FormWidget.class).fields().field("Select3")
                .control(Select.class);
        input.shouldExists();

        input.shouldBeEmpty();
        input.selectMulti(0);
        input.shouldBeChecked(0);
        input.shouldSelected("Объектов 1 шт");
        input.selectMulti(1, 2);
        input.shouldBeChecked(0, 1, 2);
        input.shouldSelected("Объектов 3 шт");
        input.clear();
        input.shouldNotBeChecked(0, 1, 2);
        input.shouldBeEmpty();
    }

    @Test
    public void testSelectFormat() {
        Select input = page.single().widget(FormWidget.class).fields().field("Select4")
                .control(Select.class);
        input.shouldExists();

        input.shouldBeEmpty();
        input.selectMulti(0);
        input.shouldBeChecked(0);
        input.shouldSelected("1 объект");
        input.selectMulti(1);
        input.shouldBeChecked(0, 1);
        input.shouldSelected("2 объекта");
        input.selectMulti(2, 3, 4);
        input.shouldBeChecked(0, 1, 2, 3, 4);
        input.shouldSelected("5 объектов");
        input.clear();
        input.shouldNotBeChecked(0, 1, 2, 3, 4);
        input.shouldBeEmpty();


        input = page.single().widget(FormWidget.class).fields().field("Select5")
                .control(Select.class);
        input.shouldExists();

        input.shouldBeEmpty();
        input.selectMulti(0);
        input.shouldSelected("Объектов 1 шт");
        input.selectMulti(1);
        input.shouldSelected("Объектов 2 шт");
        input.selectMulti(2, 3, 4);
        input.shouldSelected("Объектов 5 шт");
        input.clear();
        input.shouldBeEmpty();
    }
}
