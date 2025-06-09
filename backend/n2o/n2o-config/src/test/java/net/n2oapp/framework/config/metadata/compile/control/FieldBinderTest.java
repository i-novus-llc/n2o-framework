package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class FieldBinderTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.getEnvironment().getContextProcessor().set("test", "Test");
        builder.packs(
                new N2oPagesPack(),
                new N2oWidgetsPack(),
                new N2oFieldSetsPack(),
                new N2oControlsPack()
        );
    }

    @Test
    void testResolveProperties() {
        SimplePage page = (SimplePage) bind("net/n2oapp/framework/config/metadata/compile/control/testFieldBinder.page.xml")
                .get(new PageContext("testFieldBinder"), new DataSet());
        Form form = (Form) page.getWidget();
        Field field = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        assertThat(field.getDependencies().get(0).getExpression(), is("(function(){if (testField == 1)\nreturn 'Test';}).call(this)"));
        assertThat(field.getDependencies().get(1).getExpression(), is("(function(){if (testField == 'Test')\nreturn true;}).call(this)"));
        assertThat(field.getDependencies().get(2).getExpression(), is("(function(){if (testField == 'Test')\nreturn true;}).call(this)"));
        assertThat(field.getDependencies().get(3).getExpression(), is("(function(){if (testField == 'Test')\nreturn true;}).call(this)"));
    }

}
