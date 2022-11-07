package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oSearchButtons;
import net.n2oapp.framework.api.metadata.meta.control.SearchButtons;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;


/**
 * Компиляция компонента SearchButtons (кнопки фильтра)
 */
@Component
public class SearchButtonsCompiler extends StandardFieldCompiler<SearchButtons, N2oSearchButtons> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSearchButtons.class;
    }

    @Override
    public StandardField<SearchButtons> compile(N2oSearchButtons source, CompileContext<?, ?> context, CompileProcessor p) {
        SearchButtons field = new SearchButtons();

        field.setResetLabel(source.getResetLabel());
        field.setSearchLabel(source.getSearchLabel());
        field.setFetchOnClear(p.cast(source.getFetchOnClear(), p.resolve(
                property("n2o.api.control.searchButtons.fetch_on_clear"), Boolean.class)));
        if (source.getNoLabel() == null) {
            source.setNoLabel(true);
        }

        return compileStandardField(field, source, context, p);
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.searchButtons.src";
    }
}
