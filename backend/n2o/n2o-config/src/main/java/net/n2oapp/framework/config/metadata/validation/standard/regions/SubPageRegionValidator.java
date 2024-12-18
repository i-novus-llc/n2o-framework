package net.n2oapp.framework.config.metadata.validation.standard.regions;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.region.N2oSubPageRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getIdOrEmptyString;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

@Component
public class SubPageRegionValidator extends AbstractRegionValidator<N2oSubPageRegion> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSubPageRegion.class;
    }

    @Override
    public void validate(N2oSubPageRegion source, SourceProcessor p) {
        if (source.getDefaultPageId() != null)
            validateDefaultPageId(source);

        Arrays.stream(source.getPages()).forEach(page -> this.validatePage(page, p));

        Set<String> routes = new HashSet<>();
        for (N2oSubPageRegion.Page page : source.getPages()) {
            if (routes.contains(page.getRoute()))
                throw new N2oMetadataValidationException(
                        String.format("В элементах <sub-page> указаны повторяющиеся маршруты \"%s\"", page.getRoute()));
            routes.add(page.getRoute());
        }

        super.validate(source, p);
    }

    private void validatePage(N2oSubPageRegion.Page page, SourceProcessor p) {
        if (page.getPageId() == null)
            throw new N2oMetadataValidationException("В одном из элементов <sub-page> не указан обязательный атрибут 'page-id'");
        if (page.getRoute() == null)
            throw new N2oMetadataValidationException("В одном из элементов <sub-page> не указан обязательный атрибут 'route'");

        p.checkForExists(page.getPageId(), N2oPage.class,
                String.format("Один из элементов <sub-page> ссылается на несуществующую страницу %s",
                        getIdOrEmptyString(page.getPageId())));

        checkEmptyToolbar(page);
    }

    private void validateDefaultPageId(N2oSubPageRegion source) {
        for (N2oSubPageRegion.Page page : source.getPages())
            if (page.getPageId().equals(source.getDefaultPageId()))
                return;

        throw new N2oMetadataValidationException(
                "В атрибуте 'default-page-id' элемента <sub-page> указана страница, которая не используется ни в одном из внутренних элементов <page>");
    }

    private static void checkEmptyToolbar(N2oSubPageRegion.Page source) {
        N2oToolbar[] toolbars = source.getToolbars();
        if (isNotEmpty(toolbars))
            for (N2oToolbar toolbar : toolbars)
                if (toolbar.getItems() == null && toolbar.getGenerate() == null)
                    throw new N2oMetadataValidationException(
                            String.format("Не заданы элементы или атрибут 'generate' в тулбаре элемента <sub-page> %s",
                                    getIdOrEmptyString(source.getPageId())));
    }
}