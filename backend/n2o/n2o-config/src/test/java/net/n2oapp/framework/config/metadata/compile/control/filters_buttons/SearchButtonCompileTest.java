package net.n2oapp.framework.config.metadata.compile.control.filters_buttons;

import net.n2oapp.framework.api.metadata.meta.control.filters_buttons.SearchButton;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class SearchButtonCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV3IOPack());
        builder.compilers(new SearchButtonCompiler());
    }

    @Test
    void testSearchButton() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/control/filters_buttons/testSearchButton.page.xml")
                .get(new PageContext("testSearchButton"));

        SearchButton searchButton = (SearchButton) ((Table<?>) page.getRegions().get("single").get(0).getContent().get(0)).getFilter()
                .getFilterFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);

        assertThat(searchButton.getSrc(), is("FilterSearchButton"));
        assertThat(searchButton.getLabel(), is("Найти"));
        assertThat(searchButton.getIcon(), nullValue());
        assertThat(searchButton.getColor(), is("primary"));
        assertThat(searchButton.getDatasource(), is("testSearchButton_table1"));
        assertThat(searchButton.getBadge(), nullValue());
        assertThat(searchButton.getDescription(), nullValue());
        assertThat(searchButton.getHint(), nullValue());

        searchButton = (SearchButton) ((Table<?>) page.getRegions().get("single").get(0).getContent().get(1)).getFilter()
                .getFilterFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);

        assertThat(searchButton.getSrc(), is("FilterSearchButton"));
        assertThat(searchButton.getLabel(), is("search"));
        assertThat(searchButton.getIcon(), is("fa fa-pencil"));
        assertThat(searchButton.getColor(), is("danger"));
        assertThat(searchButton.getDatasource(), is("testSearchButton_ds"));
        assertThat(searchButton.getBadge().getText(), is("`badge`"));
        assertThat(searchButton.getBadge().getColor(), is("`color`"));
        assertThat(searchButton.getDescription(), is("`description`"));
        assertThat(searchButton.getHint(), is("`description`"));
    }
}
