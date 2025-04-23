package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.CodeViewer;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class CodeViewerCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oActionsPack(), new N2oAllDataPack(), new N2oControlsPack());
    }

    @Test
    void testCodeViewer() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testCodeViewer.page.xml")
                .get(new PageContext("testCodeViewer"));
        Form form = (Form) page.getWidget();
        Field field = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        CodeViewer codeViewer = (CodeViewer) ((StandardField) field).getControl();
        assertThat(codeViewer.getShowLineNumbers(), is(false));
        assertThat(codeViewer.getHideButtons(), is(true));
        assertThat(codeViewer.getStartingLineNumber(), is(2));
        assertThat(codeViewer.getDarkTheme(), is(true));


        field = form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        codeViewer = (CodeViewer) ((StandardField) field).getControl();
        assertThat(codeViewer.getShowLineNumbers(), is(true));
        assertThat(codeViewer.getHideButtons(), is(false));
        assertThat(codeViewer.getStartingLineNumber(), is(1));
        assertThat(codeViewer.getDarkTheme(), is(false));

    }
}
