package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.OutputList;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.n2oapp.framework.api.metadata.meta.control.OutputList.*;

/**
 * Автотест компонента вывода однострочного текста
 */
public class OutputListAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/output_list/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/output_list/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));

        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oFieldSetsPack(), new N2oControlsPack(), new N2oAllDataPack());
    }

    @Test
    public void testOutputList() {
        Fields fields = page.widget(FormWidget.class).fields();

        OutputList outputList = fields.field("output1").control(OutputList.class);
        String separator = ",";
        outputList.shouldHaveValues(separator, "test2", "test4");
        outputList.shouldHaveLinkValues(separator, "test1", "test3");
        outputList.linkShouldHaveValue("test1", "http://example.com/1");
        outputList.linkShouldHaveValue("test3", "http://example.com/2");
        outputList.shouldHaveDirection(Direction.column);

        OutputList outputList2 = fields.field("output2").control(OutputList.class);
        separator = "; ";
        outputList2.shouldHaveValues(separator, "test2", "test4");
        outputList2.shouldHaveLinkValues(separator, "test1", "test3");
        outputList2.shouldHaveDirection(Direction.row);

        OutputList outputList3 = fields.field("output3").control(OutputList.class);
        outputList3.shouldBeEmpty();
    }
}
