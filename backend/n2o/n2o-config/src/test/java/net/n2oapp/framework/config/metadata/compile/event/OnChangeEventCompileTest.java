package net.n2oapp.framework.config.metadata.compile.event;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.action.condition.ConditionAction;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.event.OnChangeEvent;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции события изменения модели данных
 */
public class OnChangeEventCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack());
    }

    @Test
    void testOnChangeEvent() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/event/testOnChangeEvent.page.xml")
                .get(new PageContext("testOnChangeEvent"));

        assertThat(page.getEvents().size(), is(2));

        OnChangeEvent event = (OnChangeEvent) page.getEvents().get(0);
        assertThat(event.getDatasource(), is("testOnChangeEvent_ds1"));
        assertThat(event.getModel(), is(ReduxModel.resolve));
        assertThat(event.getField(), is("test"));
        assertThat(event.getAction(), instanceOf(MultiAction.class));

        event = (OnChangeEvent) page.getEvents().get(1);
        assertThat(event.getDatasource(), is("testOnChangeEvent_ds1"));
        assertThat(event.getModel(), is(ReduxModel.edit));
        assertThat(event.getField(), nullValue());
        assertThat(event.getAction(), instanceOf(ConditionAction.class));

        assertThat(((ConditionAction) event.getAction()).getPayload().getDatasource(), is("testOnChangeEvent_ds1"));
        assertThat(((ConditionAction) event.getAction()).getPayload().getModel(), is(ReduxModel.edit));
    }
}
