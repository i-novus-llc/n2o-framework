package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.meta.control.Status;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oControlsV2IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции компонента отображения статуса
 */
class StatusFieldCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV2IOPack());
        builder.compilers(new StatusFieldCompiler());
    }

    @Test
    void testStatusField() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/mapping/testStatusField.page.xml")
                .get(new PageContext("testStatusField"));
        Form form = (Form) page.getWidget();
        Status status = (Status) form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        assertThat(status.getSrc(), is("StatusText"));
        assertThat(status.getColor(), is("success"));
        assertThat(status.getText(), is("Task completed"));
        assertThat(status.getTextPosition(), is(PositionEnum.LEFT));

        status = (Status) form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        assertThat(status.getSrc(), is("StatusText"));
        assertThat(status.getText(), is("test"));
        assertThat(status.getTextPosition(), is(PositionEnum.RIGHT));
    }
}