package net.n2oapp.framework.config.metadata.compile.control.filters_buttons;

import net.n2oapp.framework.api.metadata.meta.control.filters_buttons.ClearButton;
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

class ClearButtonCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV3IOPack());
        builder.compilers(new ClearButtonCompiler());
    }

    @Test
    void testClearButton() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/control/filters_buttons/testClearButton.page.xml")
                .get(new PageContext("testClearButton"));

        Table table = (Table) page.getRegions().get("single").get(0).getContent().get(0);
        ClearButton clearButton = (ClearButton) table.getFilter()
                .getFilterFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);

        assertThat(clearButton.getSrc(), is("FilterClearButton"));
        assertThat(clearButton.getLabel(), is("Сбросить"));
        assertThat(clearButton.getIcon(), nullValue());
        assertThat(clearButton.getColor(), nullValue());
        assertThat(clearButton.getDatasource(), is("testClearButton_table1"));
        assertThat(clearButton.getBadge(), nullValue());
        assertThat(clearButton.getDescription(), nullValue());
        assertThat(clearButton.getHint(), nullValue());

        table = (Table) page.getRegions().get("single").get(0).getContent().get(1);
        clearButton = (ClearButton) table.getFilter()
                .getFilterFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);

        assertThat(clearButton.getSrc(), is("FilterClearButton"));
        assertThat(clearButton.getLabel(), is("search"));
        assertThat(clearButton.getIcon(), is("fa fa-pencil"));
        assertThat(clearButton.getColor(), is("danger"));
        assertThat(clearButton.getDatasource(), is("testClearButton_ds"));
        assertThat(clearButton.getBadge().getText(), is("`badge`"));
        assertThat(clearButton.getBadge().getColor(), is("`color`"));
        assertThat(clearButton.getDescription(), is("`description`"));
        assertThat(clearButton.getHint(), is("`description`"));

        assertThat(table.getFilter().getBlackResetList().size(), is(2));

    }
}
