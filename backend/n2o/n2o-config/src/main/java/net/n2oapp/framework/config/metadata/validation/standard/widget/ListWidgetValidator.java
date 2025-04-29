package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oPagination;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShowCountTypeEnum;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;

public abstract class ListWidgetValidator<T extends N2oAbstractListWidget> extends WidgetValidator<T> {

    @Override
    public void validate(T source, SourceProcessor p) {
        super.validate(source, p);
        N2oPagination pagination = source.getPagination();
        if (pagination != null &&
                Boolean.FALSE.equals(pagination.getShowLast()) &&
                (pagination.getShowCount() == ShowCountTypeEnum.ALWAYS || pagination.getShowCount() == null))
            throw new N2oMetadataValidationException(
                    String.format("Используется некорректная комбинация атрибутов 'show-last=\"false\"' и 'show-count=\"always\"' пагинации виджета '%s'", source.getId()));
    }
}
