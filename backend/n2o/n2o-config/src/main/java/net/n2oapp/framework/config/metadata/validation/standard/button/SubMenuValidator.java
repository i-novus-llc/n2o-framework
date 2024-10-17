package net.n2oapp.framework.config.metadata.validation.standard.button;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oSubmenu;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkOnFailAction;

@Component
public class SubMenuValidator implements SourceValidator<N2oSubmenu>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSubmenu.class;
    }

    @Override
    public void validate(N2oSubmenu source, SourceProcessor p) {
        if (!ArrayUtils.isEmpty(source.getMenuItems()))
            p.safeStreamOf(source.getMenuItems()).forEach(m -> {
                p.validate(m);
                checkOnFailAction(m.getActions());
            });
        if (source.getGenerate() != null && source.getGenerate().length == 1 && StringUtils.isEmpty(source.getGenerate()[0]))
            throw new N2oMetadataValidationException(String.format("Атрибут 'generate' выпадающего меню %s не может содержать пустую строку", getLabelOrId(source)));
    }

    private String getLabelOrId(N2oSubmenu source) {
        return ValidationUtils.getIdOrEmptyString( source.getLabel() != null ? source.getLabel() : source.getId());
    }
}
