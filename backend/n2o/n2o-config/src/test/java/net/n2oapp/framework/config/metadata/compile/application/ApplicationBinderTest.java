package net.n2oapp.framework.config.metadata.compile.application;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class ApplicationBinderTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.getEnvironment().getContextProcessor().set("test", "Test");
        builder.packs(new N2oApplicationPack());
    }

    @Test
    void bindItems() {
        Application app = bind("net/n2oapp/framework/config/metadata/compile/application/testAppBinder.application.xml")
                .get(new ApplicationContext("testAppBinder"), new DataSet(), mock(SubModelsProcessor.class));

        assertThat(app.getHeader().getMenu().getItems().get(0).getTitle(), is("Test Test"));
        assertThat(app.getHeader().getMenu().getItems().get(0).getBadge().getText(), is("Test"));
        assertThat(app.getHeader().getMenu().getItems().get(0).getBadge().getColor(), is("Test"));
        assertThat(app.getHeader().getMenu().getItems().get(0).getImageSrc(), is("Test"));

        assertThat(app.getHeader().getMenu().getItems().get(1).getSubItems().get(0).getTitle(), is("Test Test"));
        assertThat(app.getHeader().getMenu().getItems().get(1).getSubItems().get(0).getBadge().getText(), is("Test"));
        assertThat(app.getHeader().getMenu().getItems().get(1).getSubItems().get(0).getBadge().getColor(), is("Test"));
        assertThat(app.getHeader().getMenu().getItems().get(1).getSubItems().get(0).getImageSrc(), is("Test"));

        assertThat(app.getSidebars().get(0).getSubtitle(), is("Test Test"));
        assertThat(app.getSidebars().get(0).getMenu().getItems().get(0).getTitle(), is("Test Test"));
        assertThat(app.getSidebars().get(0).getMenu().getItems().get(0).getBadge().getText(), is("Test"));
        assertThat(app.getSidebars().get(0).getMenu().getItems().get(0).getBadge().getColor(), is("Test"));
        assertThat(app.getSidebars().get(0).getMenu().getItems().get(0).getImageSrc(), is("Test"));

        assertThat(app.getSidebars().get(0).getMenu().getItems().get(1).getSubItems().get(0).getTitle(), is("Test Test"));
        assertThat(app.getSidebars().get(0).getMenu().getItems().get(1).getSubItems().get(0).getBadge().getText(), is("Test"));
        assertThat(app.getSidebars().get(0).getMenu().getItems().get(1).getSubItems().get(0).getBadge().getColor(), is("Test"));
        assertThat(app.getSidebars().get(0).getMenu().getItems().get(1).getSubItems().get(0).getImageSrc(), is("Test"));
    }
}
