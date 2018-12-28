package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.ControlDependency;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции кастомного контрола
 */
public class CustomFieldCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(),
                new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsPack());
    }

    @Test
    public void testField() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/control/testCustomFieldCompile.widget.xml")
                .get(new WidgetContext("testCustomFieldCompile"));
        Field field = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("testId"));
        assertThat(field.getSrc(), is("testSrc"));

        ControlDependency dependency = field.getDependencies().get(0);
        assertThat(dependency.getType(), is(ValidationType.reRender));
        assertThat(dependency.getOn().get(0), is("name"));
        assertThat(dependency.getOn().get(1), is("type"));

        assertThat(field.getJsonProperties().size(), is(3));
        assertThat(field.getJsonProperties().get("sendEmailUrl"), is("/send/email"));
        assertThat(field.getJsonProperties().get("sendCodeUrl"), is("/send/code"));
        assertThat(field.getJsonProperties().get("codeVerified"), is("{emailSender.status=='send'}"));
    }

}
