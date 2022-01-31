package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.*;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
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
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testCustomFieldCompile.page.xml")
                .get(new PageContext("testCustomFieldCompile"));
        Form form = (Form) page.getWidget();
        CustomField field = (CustomField)form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("testId"));
        assertThat(field.getSrc(), is("testSrc"));
        assertThat(field.getLabel(), is("testLabel"));
        assertThat(field.getDescription(), is("testDescription"));

        ControlDependency dependency = field.getDependencies().get(0);
        assertThat(dependency.getType(), is(ValidationType.reRender));
        assertThat(dependency.getOn().get(0), is("name"));
        assertThat(dependency.getOn().get(1), is("type"));

        assertThat(field.getJsonProperties().size(), is(3));
        assertThat(field.getJsonProperties().get("sendEmailUrl"), is("/send/email"));
        assertThat(field.getJsonProperties().get("sendCodeUrl"), is("/send/code"));
        assertThat(field.getJsonProperties().get("codeVerified"), is("{emailSender.status=='send'}"));

        assertThat(field.getControls(), nullValue());
        assertThat(field.getControl(), instanceOf(CustomControl.class));


        CustomField customField = (CustomField)form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        assertThat(customField.getId(), is("testId2"));
        assertThat(customField.getSrc(), is("StandardField"));

        assertThat(customField.getControl(), nullValue());
        assertThat(customField.getControls(), notNullValue());
        assertThat(customField.getControls().size(), is(2));

        assertThat(customField.getControls().get(0), instanceOf(SearchButtons.class));
        assertThat(customField.getControls().get(1), instanceOf(Text.class));


        CustomField customField3 = (CustomField)form.getComponent().getFieldsets().get(0).getRows().get(2).getCols().get(0).getFields().get(0);
        assertThat(customField3.getId(), is("testId3"));
        assertThat(customField3.getSrc(), is("StandardField"));
        assertThat(customField3.getControl(), instanceOf(CustomField.class));
        CustomField customField31 = (CustomField)customField3.getControl();
        assertThat(customField31.getId(), is("testId31"));
        assertThat(customField31.getSrc(), is("StandardField"));

        assertThat(customField31.getControl(), instanceOf(InputText.class));
    }

}
