package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.table.RowClick;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;

/**
 * Абстрактный биндер для связывания данных в списковых виджетах
 */
public abstract class BaseListWidgetBinder<T extends Widget> implements BaseMetadataBinder<T> {

    public void bindRowClick(RowClick rowClick, BindProcessor p) {
        if (rowClick == null || rowClick.getAction() == null)
            return;

        p.bind(rowClick.getAction());

        if (rowClick.getAction() instanceof LinkAction) {
            LinkAction linkAction = (LinkAction) rowClick.getAction();
            rowClick.setUrl(linkAction.getUrl());
            rowClick.setTarget(linkAction.getTarget());
            rowClick.setQueryMapping(linkAction.getQueryMapping());
            rowClick.setPathMapping(linkAction.getPathMapping());
            rowClick.setAction(null);
        }
    }
}
