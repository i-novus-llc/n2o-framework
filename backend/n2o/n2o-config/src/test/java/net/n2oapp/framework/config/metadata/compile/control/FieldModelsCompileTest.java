package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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
        assertThat(modalContext.getParentClientPageId(), Matchers.is("_"));
        assertThat(modalContext.getParentClientWidgetId(), Matchers.is("form"));

        Models models = read().compile().get(modalContext).getModels();

//        assertThat(models.size(), is(4));
        //parent
        assertThat(models.get("filter['modalPage_info'].parentFull").getBindLink(), is("models.filter['testForm']"));
        assertThat(models.get("filter['modalPage_info'].parentFull").getValue(), is("`testValue`"));

        assertThat(models.get("resolve['modalPage_info'].parentDefaults").getBindLink(), is("models.resolve['form']"));
        assertThat(models.get("resolve['modalPage_info'].parentDefaults").getValue(), is(nullValue()));

        //this
        assertThat(models.get("filter['modalPage_info'].thisFull").getBindLink(), is("models.filter['modalPage_testForm']"));
        assertThat(models.get("filter['modalPage_info'].thisFull").getValue(), is("`testValue`"));

        assertThat(models.get("resolve['modalPage_info'].thisDefaults").getBindLink(), is("models.resolve['modalPage_info']"));
        assertThat(models.get("resolve['modalPage_info'].thisDefaults").getValue(), is("`testValue`"));
    }

}
