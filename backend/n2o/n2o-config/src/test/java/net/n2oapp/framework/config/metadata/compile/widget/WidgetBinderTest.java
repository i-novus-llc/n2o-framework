package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование связывания виджета с данными
 */
class WidgetBinderTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.getEnvironment().getContextProcessor().set("ctxParam", "testValue");
        builder.packs(
                new N2oAllDataPack(),
                new N2oPagesPack(),
                new N2oWidgetsPack(),
                new N2oRegionsPack(),
                new N2oActionsPack(),
                new N2oCellsPack(),
                new N2oFieldSetsPack(),
                new N2oControlsPack()
        );
    }

    /**
     * Проверка резолва контекстных переменных в условиях видимости/активности виджета
     */
    @Test
    void widgetDependencyConditionResolve() {
        ReadCompileBindTerminalPipeline pipeline = bind(
                "net/n2oapp/framework/config/metadata/compile/page/testTableConditionBinder.page.xml",
                "net/n2oapp/framework/config/metadata/compile/query/testEmptyBody.query.xml");
        SimplePage page = (SimplePage) pipeline.get(new PageContext("testTableConditionBinder"), new DataSet());
        Table<?> table = (Table<?>) page.getWidget();
        assertThat(table.getDependency().getVisible().getFirst().getCondition(), is("status == 'testValue'"));
        assertThat(table.getDependency().getEnabled().getFirst().getCondition(), is("status == 'testValue'"));
    }
}
