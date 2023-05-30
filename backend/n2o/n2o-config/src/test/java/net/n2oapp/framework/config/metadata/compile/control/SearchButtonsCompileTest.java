package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.SearchButtons;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oControlsV3IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции SearchButtons (кнопки фильтра)
 */
public class SearchButtonsCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV3IOPack());
        builder.compilers(new SearchButtonsCompiler());
    }

    @Test
    void testSearchButtons() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testSearchButtons.page.xml")
                .get(new PageContext("testSearchButtons"));

        SearchButtons searchButtons = (SearchButtons) ((StandardField) ((Table<?>) page.getWidget()).getFilter()
                .getFilterFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0)).getControl();

        assertThat(searchButtons.getSrc(), is("FilterButtonsField"));
        assertThat(searchButtons.getSearchLabel(), is("search"));
        assertThat(searchButtons.getResetLabel(), is("reset"));
        assertThat(searchButtons.getFetchOnClear(), is(true));
    }
}
