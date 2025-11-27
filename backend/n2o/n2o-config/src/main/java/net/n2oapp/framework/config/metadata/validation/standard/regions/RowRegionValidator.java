package net.n2oapp.framework.config.metadata.validation.standard.regions;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oColRegion;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRowRegion;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Валидация региона {@code <row>}
 */
@Component
public class RowRegionValidator extends AbstractRegionValidator<N2oRowRegion> {

    private static final String COLUMNS_PROPERTY = "n2o.api.region.row.columns";

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oRowRegion.class;
    }

    @Override
    public void validate(N2oRowRegion source, SourceProcessor p) {
        if (source.getContent() != null) {
            int columns = castDefault(source.getColumns(), () -> p.resolve(property(COLUMNS_PROPERTY), Integer.class));
            int totalSize = getTotalSize(source, columns);
            if (totalSize > columns) {
                throw new N2oMetadataValidationException(
                        String.format("Сумма размеров колонок (%d) превышает количество колонок (%d) в <row>",
                                totalSize, columns));
            }
        }
        super.validate(source, p);
    }

    private static int getTotalSize(N2oRowRegion source, int columns) {
        int totalSize = 0;
        for (SourceComponent component : source.getContent()) {
            int colSize = (component instanceof N2oColRegion col && col.getSize() != null) ? col.getSize() : 1;
            if (colSize > columns) {
                throw new N2oMetadataValidationException(
                        String.format("Размер колонки size=\"%d\" превышает количество колонок (%d) в <row>",
                                colSize, columns));
            }
            totalSize += colSize;
        }
        return totalSize;
    }
}