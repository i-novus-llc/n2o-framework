package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oActionCell;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.ActionAware;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;

import java.util.HashMap;
import java.util.Map;

/**
 * Абстрактное связывание с данными компонента с действием
 *
 * @param <T> Компонент с действием
 */
public abstract class ActionComponentBinder<T extends ActionAware> implements BaseMetadataBinder<T> {

    @Override
    public T bind(T compiled, BindProcessor p) {
        String url = getUrl(compiled, p);
        if (compiled.getQueryMapping() != null) {
            Map<String, ModelLink> result = new HashMap<>();
            compiled.getQueryMapping().forEach((k, v) -> result.put(k, (ModelLink) p.resolveLink(v)));
            compiled.setQueryMapping(result);
        }
        compiled.setUrl(url);
        p.bind(compiled.getAction());
        return compiled;
    }

    protected String getUrl(T compiled, BindProcessor p) {
        return p.resolveUrl(compiled.getUrl(), compiled.getPathMapping(), compiled.getQueryMapping());
    }
}