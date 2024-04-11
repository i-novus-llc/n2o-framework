package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oCards;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CardsValidator extends ListWidgetValidator<N2oCards> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCards.class;
    }

    @Override
    public void validate(N2oCards source, SourceProcessor p) {
        super.validate(source, p);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), source.getDatasource());
        if (source.getContent() != null)
            Arrays.stream(source.getContent()).forEach(col -> {
                if (col.getBlocks() != null)
                    Arrays.stream(col.getBlocks()).forEach(block -> p.validate(block, widgetScope));
            });
    }
}
