package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тест {@link net.n2oapp.framework.config.metadata.compile.widget.FormBinder}
 */
class StandardDatasourceBinderTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oAllDataPack(),
                new N2oPagesPack(),
                new N2oWidgetsPack(),
                new N2oRegionsPack(),
                new N2oActionsPack(),
                new N2oFieldSetsPack(),
                new N2oControlsPack()
        );
    }

    /**
     * Проверка резолва ссылок в autoSubmit
     */
    @Test
    void autoSubmit() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/page/testFormBinderAutoSubmit.page.xml",
                "net/n2oapp/framework/config/metadata/compile/object/utAction.object.xml");
        PageContext context = new PageContext("testFormBinderAutoSubmit", "/p/w/:param0/form");
        SimplePage page = (SimplePage) pipeline.get(context, new DataSet().add("param0", "1"));
        Form form = (Form) page.getWidget();
        assertThat(((StandardDatasource) page.getDatasources().get(form.getDatasource())).getSubmit().getUrl(), containsString("/p/w/1/form"));
    }

    /**
     * Проверка, что у modelLink routable фильтра устанавливается observe=true,
     * а у обычного фильтра observe остаётся false
     */
    @Test
    void routableFilterObserve() {
        ReadCompileBindTerminalPipeline pipeline = bind(
                "net/n2oapp/framework/config/metadata/compile/datasource/testDSRoutableFilter.page.xml",
                "net/n2oapp/framework/config/metadata/compile/datasource/testDSRoutableFilter.query.xml");
        DataSet data = new DataSet();
        data.add("ds1_id", "1");
        data.add("ds1_name", "test");
        StandardPage page = (StandardPage) pipeline.get(new PageContext("testDSRoutableFilter", "/"), data);

        StandardDatasource ds = (StandardDatasource) page.getDatasources().get("_ds1");
        assertThat(ds, notNullValue());

        ModelLink routableLink = ds.getProvider().getQueryMapping().get("ds1_id");
        assertThat(routableLink, notNullValue());
        assertThat(routableLink.isObserve(), is(true));
        assertThat(routableLink.getValue(), is("`id`"));

        ModelLink commonLink = ds.getProvider().getQueryMapping().get("ds1_name");
        assertThat(commonLink, notNullValue());
        assertThat(commonLink.isObserve(), is(false));
        assertThat(commonLink.getValue(), is("test"));
    }
}
