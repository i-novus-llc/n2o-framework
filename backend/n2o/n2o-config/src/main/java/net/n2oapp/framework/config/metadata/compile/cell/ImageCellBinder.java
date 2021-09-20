package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание действия ссылки в ImageCell с данными
 */
@Component
public class ImageCellBinder implements BaseMetadataBinder<N2oImageCell> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return N2oImageCell.class;
    }

    @Override
    public N2oImageCell bind(N2oImageCell compiled, BindProcessor p) {
        compiled.setUrl(p.resolveUrl(compiled.getUrl(), compiled.getPathMapping(), compiled.getQueryMapping()));
        return compiled;
    }
}
