package net.n2oapp.framework.config.metadata.compile.control.filters_buttons;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.filter_buttons.N2oSearchButton;
import net.n2oapp.framework.api.metadata.meta.control.filters_buttons.SearchButton;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

@Component
public class SearchButtonCompiler extends AbstractFilterButtonCompiler<SearchButton, N2oSearchButton> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSearchButton.class;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.search_button.src";
    }

    @Override
    protected String getDefaultLabel(CompileProcessor p) {
        return p.getMessage("n2o.api.control.search_button.label");
    }

    @Override
    public SearchButton compile(N2oSearchButton source, CompileContext<?, ?> context, CompileProcessor p) {
        SearchButton button = new SearchButton();
        compileButton(button, source, context, p);
        button.setColor(castDefault(source.getColor(), () -> p.resolve(Placeholders.property("n2o.api.control.search_button.color"), String.class)));
        return button;
    }
}
