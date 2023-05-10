package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShowCountType;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

@Component
public class ListWidgetValidator implements SourceValidator<N2oAbstractListWidget>, SourceClassAware {

    @Override
    public void validate(N2oAbstractListWidget source, SourceProcessor p) {
        if (source.getPagination() != null &&
                Boolean.FALSE.equals(source.getPagination().getShowLast()) && ShowCountType.ALWAYS == source.getPagination().getShowCount())
            throw new N2oMetadataValidationException(
                    String.format("Используется некорректная комбинация атрибутов 'show-last=\"false\"' и 'show-count=\"always\"' пагинации виджета '%s'", source.getId()));
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAbstractListWidget.class;
    }
}
