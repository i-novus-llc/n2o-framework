package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.Hidden;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции hidden компонента (Скрытый компонент ввода)
 */
class HiddenFieldCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(),
                new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsV3IOPack(), new N2oControlsPack());
    }

    @Test
    void testField() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testHiddenFieldCompile.page.xml")
                .get(new PageContext("testHiddenFieldCompile"));
        Form form = (Form) page.getWidget();
        Hidden hidden = (Hidden) form.getComponent().getFieldsets().getFirst().getRows().getFirst().getCols().getFirst().getFields().getFirst();
        assertThat(hidden.getId(), is("testId"));
        assertThat(hidden.getSrc(), is("HiddenField"));
        assertThat(hidden.getVisible(), is((true)));
        assertThat(hidden.getEnabled(), is((true)));
        assertThat(hidden.getRequired(), is((false)));
        assertThat(hidden.getDependencies().isEmpty(), is(true));
        assertThat(hidden.getLabel(), nullValue());
    }
}