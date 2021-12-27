package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.control.OutputList;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oControlsV2IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции компонента вывода многострочного текста
 */
public class OutputListCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV2IOPack());
        builder.compilers(new OutputListCompiler());
    }

    @Test
    public void testOutputList() {
        SimplePage page =(SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testOutputList.page.xml")
                .get(new PageContext("testOutputList"));
        Form form = (Form) page.getWidget();
        OutputList outputList = (OutputList) ((StandardField) (form.getComponent().getFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0))).getControl();
        assertThat(outputList.getSrc(), is("OutputList"));
        assertThat(outputList.getLabelFieldId(), is("label"));
        assertThat(outputList.getHrefFieldId(), is("link"));
        assertThat(outputList.getTarget(), is(Target.application));
        assertThat(outputList.getDirection(), is(OutputList.Direction.row));
        assertThat(outputList.getSeparator(), is(","));

        outputList = (OutputList) ((StandardField) (form.getComponent().getFieldsets().get(0).getRows()
                .get(1).getCols().get(0).getFields().get(0))).getControl();
        assertThat(outputList.getLabelFieldId(), is("name"));
        assertThat(outputList.getHrefFieldId(), is("href"));
        assertThat(outputList.getTarget(), is(Target.newWindow));
        assertThat(outputList.getDirection(), is(OutputList.Direction.column));
        assertThat(outputList.getSeparator(), is(" "));
    }
}
