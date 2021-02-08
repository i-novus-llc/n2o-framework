package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.control.ControlDependency;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.form.FormWidgetComponent;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.fieldset.ColElementIO4;
import net.n2oapp.framework.config.io.fieldset.RowElementIO4;
import net.n2oapp.framework.config.io.fieldset.SetFieldsetElementIOv4;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oControlsPack(), new N2oActionsPack(), new N2oObjectsPack(), new N2oDataProvidersPack())
                .ios(new SetFieldsetElementIOv4(), new ColElementIO4(), new RowElementIO4())
                .compilers(new FieldSetRowCompiler(), new FieldSetColumnCompiler(), new SetFieldSetCompiler())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/fieldset/testSetFieldsetCompileWithCols.fieldset.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/fieldset/testSetFieldsetCompileWithFields.fieldset.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/fieldset/testSetFieldsetCompileWithFieldsets.fieldset.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/fieldset/testSetFieldsetCompileWithRow.fieldset.xml"))
                .mergers(new N2oFieldSetMerger());
    }

    @Test
    public void testSetFielsetWithField() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/fieldset/testFieldsetCompile.widget.xml")
                .get(new WidgetContext("testFieldsetCompile"));
        FormWidgetComponent component = form.getComponent();
        assertThat(component.getFieldsets().size(), is(4));

        FieldSet fieldSet = component.getFieldsets().get(0);
        assertThat(fieldSet.getSrc(), is("StandardFieldset"));
        assertThat(fieldSet.getLabel(), is("set"));

        List<FieldSet.Row> rows = fieldSet.getRows();
        assertThat(rows.size(), is(3));
        assertThat(rows.get(0).getCols().size(), is(1));
        assertThat(rows.get(0).getCols().get(0).getFields().size(), is(1));
        assertThat(((StandardField) rows.get(0).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput1"));
        assertThat(rows.get(1).getCols().size(), is(1));
        assertThat(rows.get(1).getCols().get(0).getFields().size(), is(1));
        assertThat(((StandardField) rows.get(1).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput2"));
        assertThat(rows.get(2).getCols().size(), is(1));
        assertThat(rows.get(2).getCols().get(0).getFields().size(), is(1));
        assertThat(((StandardField) rows.get(2).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput3"));

        rows = component.getFieldsets().get(1).getRows();
        assertThat(component.getFieldsets().get(1).getSrc(), is("StandardFieldset"));
        assertThat(rows.size(), is(4));
        assertThat(rows.get(0).getCols().size(), is(1));
        assertThat(rows.get(0).getCols().get(0).getFields().size(), is(1));
        assertThat(((StandardField) rows.get(0).getCols().get(0).getFields().get(0)).getControl().getId(), is("testCol1"));
        assertThat(rows.get(1).getCols().size(), is(1));
        assertThat(rows.get(1).getCols().get(0).getFields().size(), is(1));
        assertThat(rows.get(1).getCols().get(0).getVisible(), is(false));
        assertThat(((StandardField) rows.get(1).getCols().get(0).getFields().get(0)).getControl().getId(), is("testCol2"));
        assertThat(rows.get(2).getCols().size(), is(1));
        assertThat(rows.get(2).getCols().get(0).getFields().size(), is(1));
        assertThat(rows.get(2).getCols().get(0).getVisible(), is("`a==b`"));
        assertThat(((StandardField) rows.get(2).getCols().get(0).getFields().get(0)).getControl().getId(), is("testCol3"));
        assertThat(rows.get(3).getCols().size(), is(1));
        assertThat(rows.get(3).getCols().get(0).getFields().size(), is(1));
        assertThat(((StandardField) rows.get(3).getCols().get(0).getFields().get(0)).getControl().getId(), is("testCol4"));

        rows = component.getFieldsets().get(2).getRows();
        assertThat(component.getFieldsets().get(3).getSrc(), is("StandardFieldset"));
        assertThat(rows.size(), is(9));
        assertThat(rows.get(0).getCols().size(), is(3));
        assertThat(rows.get(0).getCols().get(0).getFields().size(), is(1));
        assertThat(((StandardField) rows.get(0).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput1"));
        assertThat(rows.get(0).getCols().get(1).getFields().size(), is(1));
        assertThat(((StandardField) rows.get(0).getCols().get(1).getFields().get(0)).getControl().getId(), is("testInput2"));
        assertThat(rows.get(0).getCols().get(2).getFields().size(), is(1));
        assertThat(((StandardField) rows.get(0).getCols().get(2).getFields().get(0)).getControl().getId(), is("testInput3"));
        assertThat(rows.get(1).getCols().size(), is(1));
        assertThat(rows.get(1).getCols().get(0).getFields().size(), is(1));
        assertThat(((StandardField) rows.get(1).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput4"));
        assertThat(rows.get(2).getCols().size(), is(1));
        assertThat(rows.get(2).getCols().get(0).getFields().size(), is(1));
        assertThat(((StandardField) rows.get(2).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput5"));
        assertThat(rows.get(3).getCols().size(), is(1));
        assertThat(((StandardField) rows.get(3).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput6"));
        assertThat(rows.get(4).getCols().size(), is(1));
        assertThat(((StandardField) rows.get(4).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput7"));
        assertThat(rows.get(5).getCols().size(), is(1));
        assertThat(rows.get(5).getCols().get(0).getFields().size(), is(1));
        assertThat(((StandardField) rows.get(5).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput8"));
        assertThat(rows.get(6).getCols().size(), is(1));
        assertThat(rows.get(6).getCols().get(0).getFields().size(), is(1));
        assertThat(((StandardField) rows.get(6).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput9"));
        assertThat(rows.get(7).getCols().size(), is(1));
        assertThat(rows.get(7).getCols().get(0).getFields().size(), is(1));
        assertThat(((StandardField) rows.get(7).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput10"));
        assertThat(rows.get(8).getCols().size(), is(1));
        assertThat(rows.get(8).getCols().get(0).getFields().size(), is(1));
        assertThat(((StandardField) rows.get(8).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput11"));

        rows = component.getFieldsets().get(3).getRows();
        assertThat(component.getFieldsets().get(3).getSrc(), is("StandardFieldset"));
        assertThat(rows.size(), is(9));
        assertThat(rows.get(0).getCols().size(), is(1));
        assertThat(rows.get(0).getCols().get(0).getFieldsets().size(), is(1));
        assertThat(rows.get(0).getCols().get(0).getFields(), nullValue());
        assertThat(rows.get(1).getCols().size(), is(1));
        assertThat(rows.get(1).getCols().get(0).getFieldsets().size(), is(1));
        assertThat(rows.get(1).getCols().get(0).getFields(), nullValue());
        assertThat(rows.get(2).getCols().size(), is(1));
        assertThat(rows.get(2).getCols().get(0).getFieldsets().size(), is(1));
        assertThat(rows.get(2).getCols().get(0).getFields(), nullValue());

        assertThat(((StandardField) rows.get(3).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput7"));
        assertThat(((StandardField) rows.get(4).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput8"));
        assertThat(rows.get(5).getCols().get(0).getFieldsets().size(), is(1));
        assertThat(rows.get(5).getCols().get(0).getFields(), nullValue());
        assertThat(((StandardField) rows.get(6).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput10"));
        assertThat(((StandardField) rows.get(7).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput11"));
        assertThat(((StandardField) rows.get(8).getCols().get(0).getFields().get(0)).getControl().getId(), is("testInput12"));
    }

    @Test
    public void testFieldSetDependency() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/fieldset/testFieldsetEVDCompile.widget.xml")
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

        fieldSet = fieldSets.get(2);
        assertThat(fieldSet.getEnabled(), is(false));
        assertThat(fieldSet.getVisible(), is(false));
        assertThat(fieldSet.getDependency(), nullValue());

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
