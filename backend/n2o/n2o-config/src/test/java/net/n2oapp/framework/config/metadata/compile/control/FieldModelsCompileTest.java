package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;

/**
 * Тестирование компиляции modelLink страницы
 */
class FieldModelsCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
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
    void testFieldModelsCompile() {
        PageContext pageContext = new PageContext("index", "/");
        Page rootPage = compile("net/n2oapp/framework/config/metadata/compile/control/fieldModelsCompileTest/index.page.xml")
                .get(pageContext);

        PageContext modalContext = (PageContext) route("/modalPage", Page.class);
        assertThat(modalContext.getParentClientPageId(), is("_"));
        assertThat(modalContext.getParentClientWidgetId(), is("_form"));

        Page page = read().compile().get(modalContext);
        Models models = page.getModels();
        assertThat(models.size(), is(11));

        //parent
        assertThat(models.get("resolve['modalPage_info'].parentFull").normalizeLink(), is("models.filter['_testForm'].testValue"));
        assertThat(models.get("resolve['modalPage_info'].parentRefField").normalizeLink(), is("models.filter['_testForm'].field"));
        assertThat(models.get("resolve['modalPage_info'].parentDefaults").normalizeLink(), is("models.resolve['_form'].testValue"));

        //this

        ModelLink link = models.get("resolve['modalPage_info'].thisFull");
        assertThat(link.normalizeLink(), is("models.filter['modalPage_testForm'].testValue"));
        assertThat(link.isObserve(), is(true));

        assertThat(models.get("resolve['modalPage_info'].thisDefaults").normalizeLink(), is("models.resolve['modalPage_info'].testValue"));

        //parent select
        link = models.get("resolve['modalPage_info'].selectParentFull");
        assertThat(link.getLink(), is("models.filter['_testForm']"));
        assertThat(((DefaultValues) link.getValue()).getValues().get("id"), is("`address.id`"));
        assertThat(((DefaultValues) link.getValue()).getValues().get("name"), is("`address.name`"));

        assertThat(models.get("resolve['modalPage_info'].selectRefField").normalizeLink(), is("models.filter['_testForm'].address"));

        link = models.get("resolve['modalPage_info'].selectParentDefaults");
        assertThat(link.getLink(), is("models.resolve['_form']"));
        assertThat(((DefaultValues) link.getValue()).getValues().get("id"), is("`address.id`"));
        assertThat(((DefaultValues) link.getValue()).getValues().get("name"), is("`address.name`"));

        //this
        link = models.get("resolve['modalPage_info'].selectThisFull");
        assertThat(link.getLink(), is("models.filter['modalPage_testForm']"));
        assertThat(((DefaultValues) link.getValue()).getValues().get("id"), is("`address.id`"));
        assertThat(((DefaultValues) link.getValue()).getValues().get("name"), is("`address.name`"));

        link = models.get("resolve['modalPage_info'].selectThisDefaults");
        assertThat(link.getLink(), is("models.resolve['modalPage_info']"));
        assertThat(((DefaultValues) link.getValue()).getValues().get("id"), is("`address.id`"));
        assertThat(((DefaultValues) link.getValue()).getValues().get("name"), is("`address.name`"));
        assertThat(link.isObserve(), is(false));

        link = models.get("resolve['modalPage_info'].id");
        assertThat(link.getLink(), is("models.resolve['modalPage_info']"));
        assertThat(link, allOf(
                hasProperty("value", is("`$.uuid()`")),
                hasProperty("observe", is(false))
        ));
    }
}
