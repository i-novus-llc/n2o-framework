package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class AbstractPageBinderTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oPagesPack(),
                new N2oWidgetsPack(), new N2oRegionsPack());
        builder.binders(new TestPageBinder("testFilteredPageBinder2"));
        builder.sources(
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/page/testFilteredPageBinder1.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/page/testFilteredPageBinder2.page.xml"));
    }

    @Test
    void fieldsResolve() {
        Page page1 = builder.read().compile().bind().get(new PageContext("testFilteredPageBinder1"), new DataSet());
        assertThat(page1.getProperties(), nullValue());
        Page page2 = builder.read().compile().bind().get(new PageContext("testFilteredPageBinder2"), new DataSet());
        assertThat(page2.getProperties().get("bind"), is(true));
    }

    public static class TestPageBinder extends AbstractFilteredPageBinder {

        public TestPageBinder(String pageId) {
            super(pageId);
        }

        @Override
        public Page bind(Page compiled, BindProcessor p) {
            HashMap<String, Object> properties = new HashMap<>();
            compiled.setProperties(properties);
            properties.put("bind", true);
            return compiled;
        }
    }
}
