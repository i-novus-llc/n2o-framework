package net.n2oapp.framework.config.metadata.compile.dependency;

import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тест на автоматическое добавление fetch для radio-group и checkbox-group
 */
class FetchDependenceCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(),
                new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsPack());
    }

    @Test
    void testFetchDependence() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/dependency/testFetchDependency.page.xml")
                .get(new PageContext("testFetchDependency"));
        Form form = (Form) page.getWidget();
        Field checkboxGrp1 = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        checkField(checkboxGrp1, "chb1", "type");

        Field checkboxGrp2 = form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        checkField(checkboxGrp2, "chb2", "type");

        Field checkboxGrp3 = form.getComponent().getFieldsets().get(0).getRows().get(2).getCols().get(0).getFields().get(0);
        assertThat(checkboxGrp3.getId(), is("chb3"));
        assertThat(checkboxGrp3.getDependencies().size(), is(0));


        Field radioGrp1 = form.getComponent().getFieldsets().get(0).getRows().get(3).getCols().get(0).getFields().get(0);
        assertThat(radioGrp1.getId(), is("rg1"));
        assertThat(radioGrp1.getDependencies().size(), is(1));
        assertThat(radioGrp1.getDependencies().get(0).getType(), is(ValidationTypeEnum.FETCH));
        assertThat(radioGrp1.getDependencies().get(0).getOn().size(), is(2));
        assertThat(radioGrp1.getDependencies().get(0).getOn(), hasItems("type","type2"));

        Field radioGrp2 = form.getComponent().getFieldsets().get(0).getRows().get(4).getCols().get(0).getFields().get(0);
        checkField(radioGrp2, "rg2", "testFieldId4");

        Field radioGrp3 = form.getComponent().getFieldsets().get(0).getRows().get(5).getCols().get(0).getFields().get(0);
        assertThat(radioGrp3.getId(), is("rg3"));
        assertThat(radioGrp3.getDependencies().size(), is(0));

        Field inputSelect1 = form.getComponent().getFieldsets().get(0).getRows().get(6).getCols().get(0).getFields().get(0);
        assertThat(inputSelect1.getId(), is("is1"));
        assertThat(inputSelect1.getDependencies().size(), is(0));

        Field inputSelect2 = form.getComponent().getFieldsets().get(0).getRows().get(7).getCols().get(0).getFields().get(0);
        checkField(inputSelect2, "is2", "testFetchOn");
    }

    private static void checkField(Field field, String id, String onType) {
        assertThat(field.getId(), is(id));
        assertThat(field.getDependencies().size(), is(1));
        assertThat(field.getDependencies().get(0).getType(), is(ValidationTypeEnum.FETCH));
        assertThat(field.getDependencies().get(0).getOn().size(), is(1));
        assertThat(field.getDependencies().get(0).getOn().get(0), is(onType));
    }

}
