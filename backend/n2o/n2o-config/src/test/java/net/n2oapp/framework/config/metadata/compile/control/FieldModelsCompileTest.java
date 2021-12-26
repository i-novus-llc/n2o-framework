package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции modelLink страницы
 */
public class FieldModelsCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/control/fieldModelsCompileTest/modal.page.xml")
        );
    }

    @Test
    public void testFieldModelsCompile() {
        PageContext pageContext = new PageContext("index", "/");
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/control/fieldModelsCompileTest/index.page.xml")
                .get(pageContext);

        PageContext modalContext = (PageContext) route("/modalPage", Page.class);
        assertThat(modalContext.getParentClientPageId(), is("_"));
        assertThat(modalContext.getParentClientWidgetId(), is("form"));

        Page page = read().compile().get(modalContext);
        Models models = page.getModels();
        assertThat(models.size(), is(10));

        //parent
        assertThat(models.get("resolve['form_modalPage_info'].parentFull").getBindLink(), is("models.filter['testForm']"));
        assertThat(models.get("resolve['form_modalPage_info'].parentFull").getValue(), is("`testValue`"));

        assertThat(models.get("resolve['form_modalPage_info'].parentRefField").getBindLink(), is("models.filter['testForm'].field"));

        assertThat(models.get("resolve['form_modalPage_info'].parentDefaults").getBindLink(), is("models.resolve['form']"));
        assertThat(models.get("resolve['form_modalPage_info'].parentDefaults").getValue(), is("`testValue`"));

        //this
        assertThat(models.get("resolve['form_modalPage_info'].thisFull").getBindLink(), is("models.filter['form_modalPage_testForm']"));
        assertThat(models.get("resolve['form_modalPage_info'].thisFull").getValue(), is("`testValue`"));

        assertThat(models.get("resolve['form_modalPage_info'].thisDefaults").getBindLink(), is("models.resolve['form_modalPage_info']"));
        assertThat(models.get("resolve['form_modalPage_info'].thisDefaults").getValue(), is("`testValue`"));

        //parent select
        assertThat(models.get("resolve['form_modalPage_info'].selectParentFull").getBindLink(), is("models.filter['testForm']"));
        assertThat(((DefaultValues)models.get("resolve['form_modalPage_info'].selectParentFull").getValue()).getValues().get("id"), is("`address.id`"));
        assertThat(((DefaultValues)models.get("resolve['form_modalPage_info'].selectParentFull").getValue()).getValues().get("name"), is("`address.name`"));

        assertThat(models.get("resolve['form_modalPage_info'].selectRefField").getBindLink(), is("models.filter['testForm'].address"));

        assertThat(models.get("resolve['form_modalPage_info'].selectParentDefaults").getBindLink(), is("models.resolve['form']"));
        assertThat(((DefaultValues)models.get("resolve['form_modalPage_info'].selectParentDefaults").getValue()).getValues().get("id"), is("`address.id`"));
        assertThat(((DefaultValues)models.get("resolve['form_modalPage_info'].selectParentDefaults").getValue()).getValues().get("name"), is("`address.name`"));

        //this
        assertThat(models.get("resolve['form_modalPage_info'].selectThisFull").getBindLink(), is("models.filter['form_modalPage_testForm']"));
        assertThat(((DefaultValues)models.get("resolve['form_modalPage_info'].selectThisFull").getValue()).getValues().get("id"), is("`address.id`"));
        assertThat(((DefaultValues)models.get("resolve['form_modalPage_info'].selectThisFull").getValue()).getValues().get("name"), is("`address.name`"));

        assertThat(models.get("resolve['form_modalPage_info'].selectThisDefaults").getBindLink(), is("models.resolve['form_modalPage_info']"));
        assertThat(((DefaultValues)models.get("resolve['form_modalPage_info'].selectThisDefaults").getValue()).getValues().get("id"), is("`address.id`"));
        assertThat(((DefaultValues)models.get("resolve['form_modalPage_info'].selectThisDefaults").getValue()).getValues().get("name"), is("`address.name`"));
    }
}
