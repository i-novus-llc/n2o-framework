package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.ColumnsButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class TableSettingsCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oActionsPack(), new N2oAllDataPack(), new N2oPagesPack(),
                new N2oFieldSetsPack(), new N2oControlsPack(), new N2oRegionsPack(), new N2oTableSettingsIOPack());
    }

    @Test
    void testTableSettings() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/testTableSettings.page.xml")
                .get(new PageContext("testTableSettings"));
        Toolbar toolbar = page.getWidget().getToolbar();
        List<Group> groups = toolbar.getGroups();
        assertThat(groups.size(), is(2));

        List<AbstractButton> buttons = groups.get(0).getButtons();
        checkButtons(buttons, AbstractButton::getHint);
        assertThat(buttons.get(9).getSrc(), is("DropdownButton"));

        buttons = ((Submenu) buttons.get(9)).getContent().stream().map(AbstractButton.class::cast).toList();
        checkButtons(buttons, AbstractButton::getLabel);
        assertThat(buttons.get(9).getSrc(), is("StandardButton"));
        assertThat(buttons.get(9).getId(), is("action1"));

        buttons = groups.get(1).getButtons();
        checkButtons(buttons, AbstractButton::getHint);
        assertThat(buttons.get(9).getSrc(), is("DropdownButton"));

        buttons = ((Submenu) buttons.get(9)).getContent().stream().map(AbstractButton.class::cast).toList();
        checkButtons(buttons, AbstractButton::getLabel);
        assertThat(buttons.get(9).getSrc(), is("StandardButton"));
        assertThat(buttons.get(9).getId(), is("action2"));

    }

    private static void checkButtons(List<AbstractButton> buttons, Function<AbstractButton, String> labelExtractor) {
        assertThat(buttons.get(0).getSrc(), is("StandardButton"));
        assertThat(labelExtractor.apply(buttons.get(0)), is("Обновить"));

        assertThat(buttons.get(1).getSrc(), is("StandardButton"));
        assertThat(labelExtractor.apply(buttons.get(1)), is("Обновить"));

        assertThat(buttons.get(2).getSrc(), is("StandardButton"));
        assertThat(labelExtractor.apply(buttons.get(2)), is("Экспортировать"));

        assertThat(buttons.get(3).getSrc(), is("ToggleColumn"));
        assertThat(labelExtractor.apply(buttons.get(3)), is("Скрытие столбцов"));
        assertThat(((ColumnsButton) buttons.get(3)).getDefaultColumns(), is("id,name,region"));
        assertThat(((ColumnsButton) buttons.get(3)).getLocked(), is("id,name"));

        assertThat(buttons.get(4).getSrc(), is("StandardButton"));
        assertThat(labelExtractor.apply(buttons.get(4)), is("Фильтры"));

        assertThat(buttons.get(5).getLabel(), is("button1"));
        assertThat(buttons.get(5).getSrc(), is("StandardButton"));

        assertThat(buttons.get(6).getSrc(), is("ChangeSize"));
        assertThat(labelExtractor.apply(buttons.get(6)), is("Количество записей"));

        assertThat(buttons.get(7).getSrc(), is("WordWrap"));
        assertThat(labelExtractor.apply(buttons.get(7)), is("Перенос по словам"));

        assertThat(buttons.get(8).getSrc(), is("ResetSettings"));
    }
}
