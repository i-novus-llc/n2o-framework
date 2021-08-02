package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oLinkCell;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Связывание данных в ячейке с ссылкой
 */
@Component
public class LinkCellBinder implements BaseMetadataBinder<N2oLinkCell> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return N2oLinkCell.class;
    }

    @Override
    public N2oLinkCell bind(N2oLinkCell compiled, BindProcessor p) {
        String url = p.resolveUrl(compiled.getUrl(), compiled.getPathMapping(), compiled.getQueryMapping());
        if (compiled.getQueryMapping() != null) {
            Map<String, ModelLink> result = new HashMap<>();
            compiled.getQueryMapping().forEach((k, v) -> result.put(k, (ModelLink) p.resolveLink(v)));
            compiled.setQueryMapping(result);
        }
        compiled.setUrl(url);
        return compiled;
    }
}
