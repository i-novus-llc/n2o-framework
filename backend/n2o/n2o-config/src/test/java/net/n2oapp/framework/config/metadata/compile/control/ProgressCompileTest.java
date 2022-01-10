package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.Progress;
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
 * Тестирование компиляции компонента отображения прогресса
 */
public class ProgressCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV2IOPack());
        builder.compilers(new ProgressCompiler());
    }

    @Test
    public void testProgress() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/field/testProgress.page.xml")
                .get(new PageContext("testProgress"));
        Form form = (Form) page.getWidget();
        Progress progress = (Progress) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0)).getControl();
        assertThat(progress.getSrc(), is("ProgressControl"));
        assertThat(progress.getMax(), is(100));
        assertThat(progress.getAnimated(), is(true));
        assertThat(progress.getStriped(), is(true));
        assertThat(progress.getBarClass(), is("class"));
        assertThat(progress.getBarText(), is("`2 + 2`"));
        assertThat(progress.getColor(), is("info"));

        progress = (Progress) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(1).getCols().get(0).getFields().get(0)).getControl();
        assertThat(progress.getAnimated(), is(false));
        assertThat(progress.getStriped(), is(false));
    }
}
