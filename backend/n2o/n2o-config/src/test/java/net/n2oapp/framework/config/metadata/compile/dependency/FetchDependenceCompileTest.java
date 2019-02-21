package net.n2oapp.framework.config.metadata.compile.dependency;

import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.compile.control.CheckboxGroupCompiler;
import net.n2oapp.framework.config.metadata.compile.control.RadioGroupCompiler;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

/**
 * Тест на автоматическое добавление fetch для radio-group и checkbox-group
 */
public class FetchDependenceCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(),
                new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack());
        builder.compilers(new CheckboxGroupCompiler(), new RadioGroupCompiler());
    }

    @Test
    public void testFetchDependence() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/dependency/testFetchDependency.widget.xml")
                .get(new WidgetContext("testFetchDependency"));

        Field checkboxGrp1 = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        assertThat(checkboxGrp1.getId(), is("chb1"));
        assertThat(checkboxGrp1.getDependencies().size(), is(1));
        assertThat(checkboxGrp1.getDependencies().get(0).getType(), is(ValidationType.fetch));
        assertThat(checkboxGrp1.getDependencies().get(0).getOn().size(), is(1));
        assertThat(checkboxGrp1.getDependencies().get(0).getOn().get(0), is("testFieldId1"));

        Field checkboxGrp2 = form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        assertThat(checkboxGrp2.getId(), is("chb2"));
        assertThat(checkboxGrp2.getDependencies().size(), is(1));
        assertThat(checkboxGrp2.getDependencies().get(0).getType(), is(ValidationType.fetch));
        assertThat(checkboxGrp2.getDependencies().get(0).getOn().size(), is(1));
        assertThat(checkboxGrp2.getDependencies().get(0).getOn().get(0), is("testFieldId2"));

        Field checkboxGrp3 = form.getComponent().getFieldsets().get(0).getRows().get(2).getCols().get(0).getFields().get(0);
        assertThat(checkboxGrp3.getId(), is("chb3"));
        assertThat(checkboxGrp3.getDependencies().size(), is(0));


        Field radioGrp1 = form.getComponent().getFieldsets().get(0).getRows().get(3).getCols().get(0).getFields().get(0);
        assertThat(radioGrp1.getId(), is("rg1"));
        assertThat(radioGrp1.getDependencies().size(), is(1));
        assertThat(radioGrp1.getDependencies().get(0).getType(), is(ValidationType.fetch));
        assertThat(radioGrp1.getDependencies().get(0).getOn().size(), is(1));
        assertThat(radioGrp1.getDependencies().get(0).getOn().get(0), is("testFieldId3"));

        Field radioGrp2 = form.getComponent().getFieldsets().get(0).getRows().get(4).getCols().get(0).getFields().get(0);
        assertThat(radioGrp2.getId(), is("rg2"));
        assertThat(radioGrp2.getDependencies().size(), is(1));
        assertThat(radioGrp2.getDependencies().get(0).getType(), is(ValidationType.fetch));
        assertThat(radioGrp2.getDependencies().get(0).getOn().size(), is(1));
        assertThat(radioGrp2.getDependencies().get(0).getOn().get(0), is("testFieldId4"));

        Field radioGrp3 = form.getComponent().getFieldsets().get(0).getRows().get(5).getCols().get(0).getFields().get(0);
        assertThat(radioGrp3.getId(), is("rg3"));
        assertThat(radioGrp3.getDependencies().size(), is(0));
    }

}
