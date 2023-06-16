package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции hidden компонента (Скрытый компонент ввода)
 */
public class HiddenFieldCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
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
    void testField() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testHiddenFieldCompile.page.xml")
                .get(new PageContext("testHiddenFieldCompile"));
        Form form = (Form) page.getWidget();
        StandardField hidden = (StandardField) form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);

        assertThat(hidden.getControl().getId(), is("testId"));
        assertThat(hidden.getControl().getSrc(), is("InputHidden"));
        assertThat(hidden.getDependencies().size(), is(0));
        assertThat(hidden.getVisible(), Matchers.is(false));
        assertThat(hidden.getEnabled(), Matchers.is(true));
        assertThat(hidden.getRequired(), Matchers.is(false));
    }
}
