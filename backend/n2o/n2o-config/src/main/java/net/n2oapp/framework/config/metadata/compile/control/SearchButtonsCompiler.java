package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oSearchButtons;
import net.n2oapp.framework.api.metadata.meta.control.SearchButtons;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция компонента SearchButtons (кнопки фильтра)
 */
@Component
public class SearchButtonsCompiler extends ComponentCompiler<SearchButtons, N2oSearchButtons> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSearchButtons.class;
    }

    @Override
    public SearchButtons compile(N2oSearchButtons source, CompileContext<?, ?> context, CompileProcessor p) {
        SearchButtons field = new SearchButtons();

        compileComponent(field, source, context, p);

        field.setId(source.getId());
        field.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.control.searchButtons.src"), String.class)));
        field.setResetLabel(source.getResetLabel());
        field.setSearchLabel(source.getSearchLabel());

        return field;
    }
}
