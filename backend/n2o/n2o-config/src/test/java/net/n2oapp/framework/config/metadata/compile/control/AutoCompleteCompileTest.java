package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.AutoComplete;
import net.n2oapp.framework.api.metadata.meta.control.ControlDependency;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции компонента ввода текста с автоподбором
 */
class AutoCompleteCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/field/testSelect.query.xml"));
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oControlsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oAllDataPack());
    }

    @Test
    void testAutoComplete() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/field/testAutoComplete.page.xml")
                .get(new PageContext("testAutoComplete"));
        Form form = (Form) page.getRegions().get("single").get(0).getContent().get(1);

        StandardField field = (StandardField) form.getComponent().getFieldsets().get(0).getRows().get(0)
                .getCols().get(0).getFields().get(0);
        AutoComplete autoComplete = (AutoComplete) field.getControl();
        assertThat(autoComplete.getSrc(), is("AutoComplete"));
        assertThat(autoComplete.getDataProvider().getUrl(), is("n2o/data/test"));
        assertThat(autoComplete.getDataProvider().getQuickSearchParam(), is("search"));
        assertThat(autoComplete.getPlaceholder(), is("`message`"));
        assertThat(autoComplete.getValueFieldId(), is("id"));
        assertThat(autoComplete.getLabelFieldId(), is("name"));
        assertThat(autoComplete.getTags(), is(true));

        Map<String, ModelLink> queryMapping = autoComplete.getDataProvider().getQueryMapping();
        assertThat(queryMapping.size(), is(2));
        assertThat(queryMapping.get("id").getValue(), is("`org_id`"));
        assertThat(queryMapping.get("id").getValue(), is("`org_id`"));
        assertThat(queryMapping.get("id").getLink(), is("models.resolve['testAutoComplete_test']"));
        assertThat(queryMapping.get("id").getDatasource(), is("testAutoComplete_test"));
        assertThat(queryMapping.get("name").getParam(), is("org_name"));

        List<ControlDependency> dependencies = field.getDependencies();
        assertThat(dependencies.size(), is(2));
        assertThat(dependencies.get(0).getType(), is(ValidationTypeEnum.FETCH));
        assertThat(dependencies.get(0).getOn(), is(Arrays.asList("auto2")));
        assertThat(dependencies.get(1).getType(), is(ValidationTypeEnum.RESET));
        assertThat(dependencies.get(1).getOn(), is(Arrays.asList("org_id")));


        autoComplete = (AutoComplete) ((StandardField) form.getComponent().getFieldsets().get(0).getRows().get(1)
                .getCols().get(0).getFields().get(0)).getControl();
        assertThat(autoComplete.getSrc(), is("AutoComplete"));
        assertThat(autoComplete.getData().get(0).get("name"), is("test1"));
        assertThat(autoComplete.getData().get(1).get("name"), is("test2"));
        assertThat(autoComplete.getValueFieldId(), is("name"));
        assertThat(autoComplete.getTags(), is(false));
        assertThat(autoComplete.getMaxTagTextLength(), is(15));


        autoComplete = (AutoComplete) ((StandardField) form.getComponent().getFieldsets().get(0).getRows().get(2)
                .getCols().get(0).getFields().get(0)).getControl();
        assertThat(autoComplete.getDatasource(), is("testAutoComplete_test"));
        assertThat(autoComplete.getData(), nullValue());
    }
}