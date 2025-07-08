package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.fieldset.MultiFieldSet;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование филдсета с динамическим числом полей
 */
class MultiFieldSetCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oAllDataPack(),
                new N2oControlsPack(), new N2oRegionsPack());
    }

    @Test
    void testMultiFieldSetWithField() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/fieldset/testMultiFieldsetCompile.page.xml")
                .get(new PageContext("testMultiFieldsetCompile"));
        Form form = (Form) page.getWidget();
        List<FieldSet> fieldsets = form.getComponent().getFieldsets();

        MultiFieldSet multiFieldSet = (MultiFieldSet) fieldsets.get(0);
        assertThat(multiFieldSet, allOf(
                hasProperty("src", is("MultiFieldset")),
                hasProperty("label", is("Заголовок")),
                hasProperty("childrenLabel", is("`'Участник '+index`")),
                hasProperty("firstChildrenLabel", is("Участник")),
                hasProperty("name", is("members")),
                hasProperty("addButtonLabel", is("Добавить участника")),
                hasProperty("removeAllButtonLabel", is("Удалить всех участников")),
                hasProperty("canRemoveFirstItem", is(true)),
                hasProperty("needAddButton", is(false)),
                hasProperty("needRemoveButton", is(false)),
                hasProperty("needRemoveAllButton", is(true)),
                hasProperty("needCopyButton", is(true)),
                hasProperty("description", is("description")),
                hasProperty("primaryKey", is("pk")),
                hasProperty("generatePrimaryKey", is(true))
        ));

        MultiFieldSet multiFieldSet2 = (MultiFieldSet) fieldsets.get(1);
        assertThat(multiFieldSet2, allOf(
                hasProperty("src", is("test")),
                hasProperty("childrenLabel", is("`members[index].name`")),
                hasProperty("firstChildrenLabel", nullValue()),
                hasProperty("canRemoveFirstItem", is(false)),
                hasProperty("needAddButton", is(true)),
                hasProperty("needRemoveButton", is(true)),
                hasProperty("needRemoveAllButton", is(false)),
                hasProperty("needCopyButton", is(false)),
                hasProperty("description", nullValue()),
                hasProperty("primaryKey", is("id")),
                hasProperty("generatePrimaryKey", is(false))
        ));

        List<Validation> validations = page.getDatasources().get(page.getWidget().getId()).getValidations().get("set1[index].input_2");
        assertThat(validations.size(), is(1));
        assertThat(((ConditionValidation) validations.get(0)).getExpression(), is("(function(){return false}).call(this)"));
        assertThat(validations.get(0).getMessage(), is("invalid"));
        validations = page.getDatasources().get(page.getWidget().getId()).getValidations().get("set1[index].set2[$index_1].input_1");
        assertThat(validations.size(), is(1));
        assertThat(((ConditionValidation) validations.get(0)).getExpression(), is("(function(){return false}).call(this)"));
        assertThat(validations.get(0).getMessage(), is("invalid"));

        multiFieldSet = (MultiFieldSet) fieldsets.get(3);
        assertThat(multiFieldSet.getBadge(), allOf(
                hasProperty("text", is("`test`")),
                hasProperty("position", hasProperty("id", is("right"))),
                hasProperty("shape", hasProperty("id", is("square"))),
                hasProperty("imagePosition", hasProperty("id", is("left"))),
                hasProperty("imageShape", hasProperty("id", is("circle")))
        ));
        assertThat(multiFieldSet, allOf(
                hasProperty("childrenLabel", is("`label1`")),
                hasProperty("firstChildrenLabel", is("`label2`")),
                hasProperty("addButtonLabel", is("`label3`")),
                hasProperty("removeAllButtonLabel", is("`label4`")),
                hasProperty("needAddButton", is("`value == 1`")),
                hasProperty("needCopyButton", is("`value == 2`")),
                hasProperty("needRemoveButton", is("`value == 3`")),
                hasProperty("needRemoveAllButton", is("`value == 4`")),
                hasProperty("canRemoveFirstItem", is("`value == 5`"))
        ));

        multiFieldSet = (MultiFieldSet) fieldsets.get(4);
        assertThat(multiFieldSet.getBadge(), allOf(
                hasProperty("text", is("text")),
                hasProperty("position", hasProperty("id", is("left"))),
                hasProperty("shape", hasProperty("id", is("circle"))),
                hasProperty("image", is("test")),
                hasProperty("imagePosition", hasProperty("id", is("right"))),
                hasProperty("imageShape", hasProperty("id", is("square")))
        ));
    }

    @Test
    void multiSetDefaultValue() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/fieldset/testMultiSetDefaultValue.page.xml")
                .get(new PageContext("testMultiSetDefaultValue"));

        assertThat(page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].id").getValue(), is("`$.uuid()`"));
        assertThat(((DefaultValues) page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].date").getValue()).getValues().get("begin"), is("2024.01.01"));
        assertThat(((DefaultValues) page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].date").getValue()).getValues().get("end"), is("2024.01.31"));
        assertThat(page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].name").getValue(), is("test"));
        assertThat(((DefaultValues) page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].type").getValue()).getValues().get("id"), is(1));
        assertThat(page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].persons[$index_1].id").getValue(), is("`$.uuid()`"));
        assertThat(((DefaultValues) page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].persons[$index_1].date").getValue()).getValues().get("begin"), is("2024.01.01"));
        assertThat(((DefaultValues) page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].persons[$index_1].date").getValue()).getValues().get("end"), is("2024.01.31"));
        assertThat(page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].persons[$index_1].name").getValue(), is("test"));
        assertThat(((DefaultValues) page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].persons[$index_1].type").getValue()).getValues().get("id"), is(1));
    }
}
