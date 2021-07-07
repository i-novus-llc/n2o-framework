package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.meta.control.Control;
import net.n2oapp.framework.api.metadata.meta.control.Hidden;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции hidden компонента (Скрытый компонент ввода)
 */
public class HiddenFieldCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(),
                new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack(), new N2oControlsPack());
    }

    @Test
    public void testField() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/control/testHiddenFieldCompile.widget.xml")
                .get(new WidgetContext("testHiddenFieldCompile"));
        StandardField hidden = (StandardField) form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);

        assertThat(hidden.getControl().getId(), is("testId"));
        assertThat(hidden.getControl().getSrc(), is("InputHidden"));
        assertThat(hidden.getDependencies().size(), is(1));
        assertThat(hidden.getDependencies().get(0).getType(), is(ValidationType.visible));
        assertThat(hidden.getDependencies().get(0).getOn().isEmpty(), is(true));
        assertThat(hidden.getDependencies().get(0).getExpression(), is("false"));
    }
}
