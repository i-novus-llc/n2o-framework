package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.badge.Position;
import net.n2oapp.framework.api.metadata.meta.control.OutputText;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oControlsV3IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции компонента вывода однострочного текста
 */
public class OutputTextCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV3IOPack());
        builder.compilers(new OutputTextCompiler());
    }

    @Test
    void testOutputText() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testOutputText.page.xml")
                .get(new PageContext("testOutputText"));
        Form form = (Form) page.getWidget();
        OutputText outputText = (OutputText) ((StandardField) (form.getComponent().getFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0))).getControl();
        assertThat(outputText.getSrc(), is("OutputText"));
        assertThat(outputText.getIcon(), is("icon"));
        assertThat(outputText.getIconPosition(), is(Position.RIGHT));
        assertThat(outputText.getFormat(), is("number 0,0.00"));
        assertThat(outputText.getEllipsis(), is(false));
        assertThat(outputText.getExpandable(), is(false));

        outputText = (OutputText) ((StandardField) (form.getComponent().getFieldsets().get(0).getRows()
                .get(1).getCols().get(0).getFields().get(0))).getControl();
        assertThat(outputText.getIcon(), nullValue());
        assertThat(outputText.getIconPosition(), is(Position.LEFT));
        assertThat(outputText.getFormat(), nullValue());
        assertThat(outputText.getEllipsis(), is(false));
        assertThat(outputText.getExpandable(), is(false));
    }
}
