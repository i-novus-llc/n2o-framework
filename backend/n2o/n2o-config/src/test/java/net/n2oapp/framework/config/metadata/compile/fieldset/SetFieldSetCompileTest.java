package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.control.ControlDependency;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.fieldset.LineFieldSet;
import net.n2oapp.framework.api.metadata.meta.fieldset.SetFieldSet;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование простого филдсета
 */
public class SetFieldSetCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oActionsPack(), new N2oObjectsPack(), new N2oDataProvidersPack());
    }

    @Test
    public void testFieldSetWithFields() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/fieldset/testSetFieldsetCompileWithFields.widget.xml")
                .get(new WidgetContext("testSetFieldsetCompileWithFields"));

        List<FieldSet> fieldsets = form.getComponent().getFieldsets();
        assertThat(fieldsets.size(), is(2));

        assertThat(fieldsets.get(0).getRows().size(), is(2));
        assertThat(fieldsets.get(0).getRows().get(0).getCols().get(0).getFields().get(0).getId(), is("id1"));
        assertThat(fieldsets.get(0).getRows().get(1).getCols().get(0).getFields().get(0).getId(), is("id2"));

        assertThat(fieldsets.get(1), instanceOf(LineFieldSet.class));
        List<FieldSet.Row> fieldSetRows = fieldsets.get(1).getRows();
        assertThat(fieldSetRows.size(), is(3));
        assertThat(fieldSetRows.get(0).getCols().get(0).getFields().get(0).getId(), is("id3"));
        assertThat(fieldSetRows.get(1).getCols().get(0).getFields().get(0).getId(), is("id4"));
        assertThat(fieldSetRows.get(2).getCols().get(0).getFieldsets().get(0), instanceOf(LineFieldSet.class));
        assertThat(fieldSetRows.get(2).getCols().get(0).getFieldsets().get(0).getRows().size(), is(2));
        assertThat(fieldSetRows.get(2).getCols().get(0).getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0).getId(), is("id5"));
        assertThat(fieldSetRows.get(2).getCols().get(0).getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0).getId(), is("id6"));
    }

    @Test
    public void testFieldSetRows() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/fieldset/testSetFieldsetCompileWithRow.widget.xml")
                .get(new WidgetContext("testSetFieldsetCompileWithRow"));

        List<FieldSet.Row> rows = form.getComponent().getFieldsets().get(0).getRows();
        assertThat(rows.size(), is(2));

        FieldSet.Row row = rows.get(0);
        assertThat(row.getCols().size(), is(2));
        assertThat(row.getCols().get(0).getFields().size(), is(1));
        assertThat(row.getCols().get(0).getFields().get(0).getId(), is("id1"));
        assertThat(row.getCols().get(1).getFields().size(), is(1));
        assertThat(row.getCols().get(1).getFields().get(0).getId(), is("id2"));

        row = rows.get(1);
        assertThat(row.getCols().size(), is(4));
        assertThat(row.getCols().get(0).getFields().size(), is(1));
        assertThat(row.getCols().get(0).getFields().get(0).getId(), is("id3"));
        assertThat(row.getCols().get(1).getFields().size(), is(1));
        assertThat(row.getCols().get(1).getFields().get(0).getId(), is("id4"));
        assertThat(row.getCols().get(2).getFieldsets().get(0), instanceOf(SetFieldSet.class));
        assertThat(row.getCols().get(2).getFieldsets().get(0).getRows().get(0).getCols().size(), is(2));
        assertThat(row.getCols().get(2).getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0).getId(), is("id5"));
        assertThat(row.getCols().get(2).getFieldsets().get(0).getRows().get(0).getCols().get(1).getFields().get(0).getId(), is("id6"));
        assertThat(row.getCols().get(3).getFieldsets().get(0), instanceOf(LineFieldSet.class));
        assertThat(row.getCols().get(3).getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0).getId(), is("id7"));
    }

    @Test
    public void testFieldSetCols() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/fieldset/testSetFieldsetCompileWithCols.widget.xml")
                .get(new WidgetContext("testSetFieldsetCompileWithCols"));

        List<FieldSet.Row> rows = form.getComponent().getFieldsets().get(0).getRows();
        assertThat(rows.size(), is(2));

        FieldSet.Row row = rows.get(0);
        assertThat(row.getCols().size(), is(1));
        assertThat(row.getCols().get(0).getVisible(), is("`a==b`"));
        assertThat(row.getCols().get(0).getFields().size(), is(2));
        assertThat(row.getCols().get(0).getFields().get(0).getId(), is("id1"));
        assertThat(row.getCols().get(0).getFields().get(1).getId(), is("id2"));

        row = rows.get(1);
        assertThat(row.getCols().size(), is(1));
        assertThat(row.getCols().get(0).getFieldsets().size(), is(2));
        List<FieldSet.Row> fieldSetRows = row.getCols().get(0).getFieldsets().get(0).getRows();
        assertThat(fieldSetRows.size(), is(4));
        assertThat(fieldSetRows.get(0).getCols().get(0).getFields().get(0).getId(), is("id3"));
        assertThat(fieldSetRows.get(1).getCols().get(0).getFields().get(0).getId(), is("id4"));
        assertThat(fieldSetRows.get(2).getCols().size(), is(2));
        assertThat(fieldSetRows.get(2).getCols().get(0).getFields().get(0).getId(), is("id5"));
        assertThat(fieldSetRows.get(2).getCols().get(1).getFields().get(0).getId(), is("id6"));
        assertThat(fieldSetRows.get(3).getCols().get(0).getFields().get(0).getId(), is("id7"));

        assertThat(row.getCols().get(0).getFieldsets().get(1), instanceOf(LineFieldSet.class));
    }

    @Test
    public void testFieldSetDependency() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/fieldset/testFieldsetEVDCompile.widget.xml",
                "net/n2oapp/framework/config/metadata/compile/fieldset/testSetFieldsetCompileWithFields.fieldset.xml")
                .get(new WidgetContext("testFieldsetEVDCompile"));
        List<FieldSet> fieldSets = form.getComponent().getFieldsets();
        assertThat(fieldSets.size(), is(4));

        FieldSet fieldSet = fieldSets.get(0);
        assertThat(fieldSet.getEnabled(), nullValue());
        assertThat(fieldSet.getVisible(), nullValue());
        assertThat(fieldSet.getDependency(), nullValue());

        fieldSet = fieldSets.get(1);
        assertThat(fieldSet.getEnabled(), is(true));
        assertThat(fieldSet.getVisible(), is(true));
        assertThat(fieldSet.getDependency(), nullValue());
        assertThat(fieldSet.getDescription(), is("description"));

        fieldSet = fieldSets.get(2);
        assertThat(fieldSet.getEnabled(), is(false));
        assertThat(fieldSet.getVisible(), is(false));
        assertThat(fieldSet.getDependency(), nullValue());
        assertThat(fieldSet.getDescription(), nullValue());

        fieldSet = fieldSets.get(3);
        assertThat(fieldSet.getEnabled(), is("`x < 5`"));
        assertThat(fieldSet.getVisible(), is("`x > 1`"));
        ControlDependency dependency = fieldSet.getDependency()[0];
        assertThat(dependency.getOn(), is(Arrays.asList("a.b.c", "z.x.c")));
        assertThat(dependency.getType().toString(), is("reRender"));
    }

    /**
     * Проверяется, что условия видимости полей переходят
     * в enablingConditions соответствующих этим полям валидаций
     */
    @Test
    public void testValidationEnabling() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = compile(
                "net/n2oapp/framework/config/metadata/compile/fieldset/testFieldsetVisibility.page.xml",
                "net/n2oapp/framework/config/metadata/compile/fieldset/testFieldsetVisibility.object.xml"
        );
        pipeline.get(new PageContext("testFieldsetVisibility"));
        ActionContext context = (ActionContext) builder.route("/testFieldsetVisibility/action", CompiledObject.class, null);

        assertThat(context, notNullValue());

        CompiledObject object = pipeline.get(context);

        List<Validation> validations = object.getOperations().get("test").getValidationList();
        Validation validation = validations.get(1);
        assertThat(validation.getId(), is("id1"));
        assertThat(validation.getEnablingConditions().size(), is(0));

        validation = validations.get(2);
        assertThat(validation.getId(), is("id2"));
        assertThat(validation.getEnablingConditions().size(), is(1));
        assertThat(validation.getEnablingConditions().get(0), is("{fieldset1Condition}"));

        validation = validations.get(3);
        assertThat(validation.getId(), is("id3"));
        assertThat(validation.getEnablingConditions().size(), is(1));
        assertThat(validation.getEnablingConditions().get(0), is("{fieldset1Condition}"));

        validation = validations.get(4);
        assertThat(validation.getId(), is("id4"));
        assertThat(validation.getEnablingConditions().size(), is(3));
        assertThat(validation.getEnablingConditions().get(0), is("{id4Condition}"));
        assertThat(validation.getEnablingConditions().get(1), is("{fieldset2Condition}"));
        assertThat(validation.getEnablingConditions().get(2), is("{fieldset1Condition}"));

        validation = validations.get(5);
        assertThat(validation.getId(), is("id5Required2"));
        assertThat(validation.getEnablingConditions().size(), is(2));
        assertThat(validation.getEnablingConditions().get(0), is("{fieldset3Condition}"));
        assertThat(validation.getEnablingConditions().get(1), is("{fieldset1Condition}"));

        validation = validations.get(6);
        assertThat(validation.getId(), is("id5IsNotNull"));
        assertThat(validation.getEnablingConditions().size(), is(2));
        assertThat(validation.getEnablingConditions().get(0), is("{fieldset3Condition}"));
        assertThat(validation.getEnablingConditions().get(1), is("{fieldset1Condition}"));
    }
}
