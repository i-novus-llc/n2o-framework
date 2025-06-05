package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oTiles;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TilesValidator extends ListWidgetValidator<N2oTiles> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTiles.class;
    }

    @Override
    public void validate(N2oTiles source, SourceProcessor p) {
        super.validate(source, p);
        MetaActions pageActions = p.getScope(MetaActions.class);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), source.getDatasource(),
                getAllMetaActions(pageActions, source.getActions(), p));
        if (source.getContent() != null)
            Arrays.stream(source.getContent()).forEach(col -> p.validate(col, widgetScope));
    }
}
