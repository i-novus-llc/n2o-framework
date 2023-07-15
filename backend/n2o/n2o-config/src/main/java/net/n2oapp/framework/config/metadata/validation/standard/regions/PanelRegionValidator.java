package net.n2oapp.framework.config.metadata.validation.standard.regions;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.compile.enums.Color;
import net.n2oapp.framework.api.metadata.global.view.region.N2oPanelRegion;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

@Component
public class PanelRegionValidator implements SourceValidator<N2oPanelRegion>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPanelRegion.class;
    }

    @Override
    public void validate(N2oPanelRegion source, SourceProcessor p) {
        if (source.getColor() != null &&
                !EnumUtils.isValidEnum(Color.class, source.getColor())) {
            throw new N2oMetadataValidationException(
                    String.format("В регионе <panel> '%s' указано недопустимое значение атрибута color=\"%s\"", source.getId(), source.getColor()));
        }
    }
}
