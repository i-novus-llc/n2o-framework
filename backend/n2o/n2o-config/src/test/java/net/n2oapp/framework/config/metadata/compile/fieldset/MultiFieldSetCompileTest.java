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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

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
        assertThat(multiFieldSet.getSrc(), is("MultiFieldset"));
        assertThat(multiFieldSet.getLabel(), is("Заголовок"));
        assertThat(multiFieldSet.getChildrenLabel(), is("`'Участник '+index`"));
        assertThat(multiFieldSet.getFirstChildrenLabel(), is("Участник"));
        assertThat(multiFieldSet.getName(), is("members"));
        assertThat(multiFieldSet.getAddButtonLabel(), is("Добавить участника"));
        assertThat(multiFieldSet.getRemoveAllButtonLabel(), is("Удалить всех участников"));
        assertThat(multiFieldSet.getCanRemoveFirstItem(), is(true));
        assertThat(multiFieldSet.getNeedAddButton(), is(false));
        assertThat(multiFieldSet.getNeedRemoveButton(), is(false));
        assertThat(multiFieldSet.getNeedRemoveAllButton(), is(true));
        assertThat(multiFieldSet.getNeedCopyButton(), is(true));
        assertThat(multiFieldSet.getDescription(), is("description"));
        assertThat(multiFieldSet.getPrimaryKey(), is("pk"));
        assertThat(multiFieldSet.getGeneratePrimaryKey(), is(true));

        MultiFieldSet multiFieldSet2 = (MultiFieldSet) fieldsets.get(1);
        assertThat(multiFieldSet2.getSrc(), is("test"));
        assertThat(multiFieldSet2.getChildrenLabel(), is("`members[index].name`"));
        assertThat(multiFieldSet2.getFirstChildrenLabel(), is(nullValue()));
        assertThat(multiFieldSet2.getCanRemoveFirstItem(), is(false));
        assertThat(multiFieldSet2.getNeedAddButton(), is(true));
        assertThat(multiFieldSet2.getNeedRemoveButton(), is(true));
        assertThat(multiFieldSet2.getNeedRemoveAllButton(), is(false));
        assertThat(multiFieldSet2.getNeedCopyButton(), is(false));
        assertThat(multiFieldSet2.getDescription(), nullValue());
        assertThat(multiFieldSet2.getPrimaryKey(), is("id"));
        assertThat(multiFieldSet2.getGeneratePrimaryKey(), is(false));

        List<Validation> validations = page.getDatasources().get(page.getWidget().getId()).getValidations().get("set1[index].input_2");
        assertThat(validations.size(), is(1));
        assertThat(((ConditionValidation) validations.get(0)).getExpression(), is("(function(){return false}).call(this)"));
        assertThat(validations.get(0).getMessage(), is("invalid"));
        validations = page.getDatasources().get(page.getWidget().getId()).getValidations().get("set1[index].set2[$index_1].input_1");
        assertThat(validations.size(), is(1));
        assertThat(((ConditionValidation) validations.get(0)).getExpression(), is("(function(){return false}).call(this)"));
        assertThat(validations.get(0).getMessage(), is("invalid"));

        multiFieldSet = (MultiFieldSet) fieldsets.get(3);
        assertThat(multiFieldSet.getBadge().getText(), is("`test`"));
        assertThat(multiFieldSet.getBadge().getPosition().getId(), is("right"));
        assertThat(multiFieldSet.getBadge().getShape().getId(), is("square"));
        assertThat(multiFieldSet.getBadge().getImagePosition().getId(), is("left"));
        assertThat(multiFieldSet.getBadge().getImageShape().getId(), is("circle"));
        assertThat(multiFieldSet.getChildrenLabel(), is("`label1`"));
        assertThat(multiFieldSet.getFirstChildrenLabel(), is("`label2`"));
        assertThat(multiFieldSet.getAddButtonLabel(), is("`label3`"));
        assertThat(multiFieldSet.getRemoveAllButtonLabel(), is("`label4`"));
        assertThat(multiFieldSet.getNeedAddButton(), is("`value == 1`"));
        assertThat(multiFieldSet.getNeedCopyButton(), is("`value == 2`"));
        assertThat(multiFieldSet.getNeedRemoveButton(), is("`value == 3`"));
        assertThat(multiFieldSet.getNeedRemoveAllButton(), is("`value == 4`"));
        assertThat(multiFieldSet.getCanRemoveFirstItem(), is("`value == 5`"));

        multiFieldSet = (MultiFieldSet) fieldsets.get(4);
        assertThat(multiFieldSet.getBadge().getText(), is("text"));
        assertThat(multiFieldSet.getBadge().getPosition().getId(), is("left"));
        assertThat(multiFieldSet.getBadge().getShape().getId(), is("circle"));
        assertThat(multiFieldSet.getBadge().getImage(), is("test"));
        assertThat(multiFieldSet.getBadge().getImagePosition().getId(), is("right"));
        assertThat(multiFieldSet.getBadge().getImageShape().getId(), is("square"));
    }

    @Test
    void multiSetDefaultValue() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/fieldset/testMultiSetDefaultValue.page.xml")
                .get(new PageContext("testMultiSetDefaultValue"));

        assertThat(page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].id").getValue(), is("`$.uuid()`"));
        assertThat(((DefaultValues)page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].date").getValue()).getValues().get("begin"), is("2024.01.01"));
        assertThat(((DefaultValues)page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].date").getValue()).getValues().get("end"), is("2024.01.31"));
        assertThat(page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].name").getValue(), is("test"));
        assertThat(((DefaultValues)page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].type").getValue()).getValues().get("id"), is(1));
        assertThat(page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].persons[$index_1].id").getValue(), is("`$.uuid()`"));
        assertThat(((DefaultValues)page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].persons[$index_1].date").getValue()).getValues().get("begin"), is("2024.01.01"));
        assertThat(((DefaultValues)page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].persons[$index_1].date").getValue()).getValues().get("end"), is("2024.01.31"));
        assertThat(page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].persons[$index_1].name").getValue(), is("test"));
        assertThat(((DefaultValues)page.getModels().get("resolve['testMultiSetDefaultValue_ds1'].groups[index].persons[$index_1].type").getValue()).getValues().get("id"), is(1));
    }
}
