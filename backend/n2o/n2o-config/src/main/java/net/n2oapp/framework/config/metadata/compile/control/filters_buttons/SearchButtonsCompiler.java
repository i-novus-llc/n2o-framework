package net.n2oapp.framework.config.metadata.compile.control.filters_buttons;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.filter_buttons.N2oClearButton;
import net.n2oapp.framework.api.metadata.control.filter_buttons.N2oSearchButton;
import net.n2oapp.framework.api.metadata.control.filter_buttons.N2oSearchButtons;
import net.n2oapp.framework.api.metadata.meta.control.filters_buttons.ClearButton;
import net.n2oapp.framework.api.metadata.meta.control.filters_buttons.SearchButton;
import net.n2oapp.framework.api.metadata.meta.control.filters_buttons.SearchButtons;
import net.n2oapp.framework.config.metadata.compile.control.FieldCompiler;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция компонента SearchButtons (кнопки фильтра)
 */
@Component
public class SearchButtonsCompiler extends FieldCompiler<SearchButtons, N2oSearchButtons> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSearchButtons.class;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.search_buttons.src";
    }

    @Override
    public SearchButtons compile(N2oSearchButtons source, CompileContext<?, ?> context, CompileProcessor p) {
        SearchButtons field = new SearchButtons();
        source.setNoLabel(p.resolve(property("n2o.api.control.search_buttons.no_label"), String.class));
        field.setSearch(initSearchButton(source, context, p));
        field.setClear(initClearButton(source, context, p));
        compileField(field, source, context, p);
        return field;
    }

    private SearchButton initSearchButton(N2oSearchButtons source, CompileContext<?, ?> context, CompileProcessor p) {
        N2oSearchButton searchButton = new N2oSearchButton();
        searchButton.setLabel(source.getSearchLabel());
        searchButton.setNoLabelBlock(source.getNoLabelBlock());
        return p.compile(searchButton, context);
    }

    private ClearButton initClearButton(N2oSearchButtons source, CompileContext<?, ?> context, CompileProcessor p) {
        N2oClearButton clearButton = new N2oClearButton();
        clearButton.setLabel(source.getResetLabel());
        clearButton.setNoLabelBlock(source.getNoLabelBlock());
        clearButton.setIgnore(source.getClearIgnore());
        return p.compile(clearButton, context);
    }

    @Override
    protected String initLabel(N2oSearchButtons source, CompileProcessor p) {
        return null;
    }
}
