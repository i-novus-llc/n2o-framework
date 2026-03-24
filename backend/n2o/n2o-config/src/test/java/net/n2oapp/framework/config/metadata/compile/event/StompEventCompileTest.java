package net.n2oapp.framework.config.metadata.compile.event;

import net.n2oapp.framework.api.metadata.meta.event.StompEvent;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции STOMP-события на странице
 */
class StompEventCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack());
    }

    @Test
    void testStompEvent() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/event/testStompEvent.page.xml")
                .get(new PageContext("testStompEvent"));

        assertThat(page.getEvents().size(), is(2));

        StompEvent event = (StompEvent) page.getEvents().getFirst();
        assertThat(event.getId(), is("notif1"));
        assertThat(event.getDestination(), is("/notifications"));

        event = (StompEvent) page.getEvents().get(1);
        assertThat(event.getId(), is("ev0"));
        assertThat(event.getDestination(), is("/updates"));
    }
}
