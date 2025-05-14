package net.n2oapp.framework.config.metadata.compile.control.filters_buttons;

import net.n2oapp.framework.api.metadata.meta.control.filters_buttons.ClearButton;
import net.n2oapp.framework.api.metadata.meta.control.filters_buttons.SearchButton;
import net.n2oapp.framework.api.metadata.meta.control.filters_buttons.SearchButtons;
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

/**
 * Тестирование компиляции SearchButtons (кнопки фильтра)
 */
class SearchButtonsCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV3IOPack());
        builder.compilers(new SearchButtonsCompiler(), new SearchButtonCompiler(), new ClearButtonCompiler());
    }

    @Test
    void testSearchButtons() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/control/filters_buttons/testSearchButtons.page.xml")
                .get(new PageContext("testSearchButtons"));

        SearchButtons searchButtons = (SearchButtons) ((Table<?>) page.getRegions().get("single").get(0).getContent().get(0)).getFilter()
                .getFilterFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);

        SearchButton searchButton = searchButtons.getSearch();
        ClearButton clearButton = searchButtons.getClear();

        assertThat(searchButtons.getSrc(), is("FilterButtons"));

        assertThat(searchButton.getSrc(), is("FilterSearchButton"));
        assertThat(searchButton.getLabel(), is("Найти"));
        assertThat(searchButton.getIcon(), nullValue());
        assertThat(searchButton.getColor(), is("primary"));
        assertThat(searchButton.getDatasource(), is("testSearchButtons_w1"));
        assertThat(searchButton.getBadge(), nullValue());
        assertThat(searchButton.getDescription(), nullValue());
        assertThat(searchButton.getHint(), nullValue());

        assertThat(clearButton.getSrc(), is("FilterClearButton"));
        assertThat(clearButton.getLabel(), is("Сбросить"));
        assertThat(clearButton.getIcon(), nullValue());
        assertThat(clearButton.getColor(), nullValue());
        assertThat(clearButton.getDatasource(), is("testSearchButtons_w1"));
        assertThat(clearButton.getBadge(), nullValue());
        assertThat(clearButton.getDescription(), nullValue());
        assertThat(clearButton.getHint(), nullValue());

        searchButtons = (SearchButtons) ((Table<?>) page.getRegions().get("single").get(0).getContent().get(1)).getFilter()
                .getFilterFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        searchButton = searchButtons.getSearch();
        clearButton = searchButtons.getClear();

        assertThat(searchButton.getSrc(), is("FilterSearchButton"));
        assertThat(searchButton.getLabel(), is("search"));
        assertThat(searchButton.getNoLabelBlock(), is(true));
        assertThat(clearButton.getSrc(), is("FilterClearButton"));
        assertThat(clearButton.getLabel(), is("reset"));
        assertThat(clearButton.getNoLabelBlock(), is(true));
    }
}
