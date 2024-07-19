package net.n2oapp.framework.config.metadata.validation.standard.button;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор исходной модели кнопки
 */
@Component
public class ButtonValidator implements SourceValidator<N2oButton>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oButton.class;
    }

    @Override
    public void validate(N2oButton source, SourceProcessor p) {
        checkValidateDependenciesDatasource(source, p);
    }

    /**
     * Проверка существования источников данных в зависимостях
     *
     * @param source Исходная модель кнопки
     */
    private void checkValidateDependenciesDatasource(N2oButton source, SourceProcessor p) {
        if (source.getDependencies() != null) {
            for ( N2oButton.Dependency dependency : source.getDependencies()) {
                if (dependency.getDatasource() != null) {
                    ValidationUtils.checkDatasourceExistence(dependency.getDatasource(), p,
                            String.format("Атрибут 'datasource' в зависимостях кнопки %s ссылается несуществующий источник данных '%s'",
                                    getLabelOrId(source), dependency.getDatasource()));
                }
            }
        }
    }

    private String getLabelOrId(Button button) {
        return ValidationUtils.getIdOrEmptyString(button.getLabel() != null ? button.getLabel() : button.getId());
    }
}
