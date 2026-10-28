package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oHtmlWidget;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор html виджета
 */
@Component
public class HtmlWidgetValidator extends WidgetValidator<N2oHtmlWidget> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oHtmlWidget.class;
    }

    @Override
    public void validate(N2oHtmlWidget source, SourceProcessor p) {
        super.validate(source, p);
        ValidationUtils.checkBodyOrFile("<content>", source.getHtml(), source.getFile(),
                "виджета " + ValidationUtils.getIdOrEmptyString(source.getId()));
    }
}