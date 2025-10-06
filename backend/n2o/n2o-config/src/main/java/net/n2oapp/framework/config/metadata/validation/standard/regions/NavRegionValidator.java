package net.n2oapp.framework.config.metadata.validation.standard.regions;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oNavRegion;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидация региона {@code <nav>}
 */
@Component
public class NavRegionValidator extends AbstractRegionValidator<N2oNavRegion> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oNavRegion.class;
    }

    @Override
    public void validate(N2oNavRegion source, SourceProcessor p) {
        if (source.getDatasourceId() != null)
            ValidationUtils.checkDatasourceExistence(source.getDatasourceId(), p,
                    String.format("Регион <nav> ссылается на несуществующий источник данных %s",
                            ValidationUtils.getIdOrEmptyString(source.getDatasourceId())));
        p.safeStreamOf(source.getMenuItems()).forEach(p::validate);
    }
}