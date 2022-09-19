package net.n2oapp.framework.config.metadata.compile.dataprovider;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.compile.SourceTransformer;
import net.n2oapp.framework.api.metadata.dataprovider.N2oGraphQlDataProvider;
import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.nonNull;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static org.springframework.util.StringUtils.hasText;

/**
 * Заголовки для проброса с клиента из настройки n2o.api.data-provider.forward-headers, парсинг строки с заголовками в коллекцию
 */
@Component
public class DataProviderForwardHeadersTransformer implements SourceTransformer<N2oQuery>, SourceClassAware {

    @Override
    public N2oQuery transform(N2oQuery source, SourceProcessor p) {
        resolveHeaders(source.getCounts(), p);
        resolveHeaders(source.getLists(), p);
        resolveHeaders(source.getUniques(), p);
        return source;
    }

    @Override
    public boolean matches(N2oQuery source) {
        return hasHeadersToForward(source.getLists())
                && hasHeadersToForward(source.getUniques())
                && hasHeadersToForward(source.getCounts());
    }

    private void resolveHeaders(N2oQuery.Selection[] selections, SourceProcessor p) {
        if (nonNull(selections))
            Arrays.stream(selections).forEach(selection -> resolveHeaders(selection, p));
    }

    private void resolveHeaders(N2oQuery.Selection selection, SourceProcessor p) {
        if (!hasHeadersToForward(selection))
            return;
        N2oInvocation invocation = selection.getInvocation();
        String forwardedHeadersProperty = p.resolve(property("n2o.api.data-provider.forward-headers"), String.class);
        String forwardedHeaders = invocation instanceof N2oRestDataProvider ? ((N2oRestDataProvider) invocation).getForwardedHeaders() : ((N2oGraphQlDataProvider) invocation).getForwardedHeaders();

        HashSet<String> forwardedHeadersResult = new HashSet<>();
        if (hasText(forwardedHeadersProperty))
            parseHeadersString(forwardedHeadersProperty, forwardedHeadersResult);
        if (hasText(forwardedHeaders))
            parseHeadersString(forwardedHeaders, forwardedHeadersResult);

        if (invocation instanceof N2oRestDataProvider)
            ((N2oRestDataProvider) invocation).setForwardedHeadersSet(forwardedHeadersResult);
        else ((N2oGraphQlDataProvider) invocation).setForwardedHeadersSet(forwardedHeadersResult);
    }

    private void parseHeadersString(String headers, Set<String> result) {
        for (String forwardedHeaderName : headers.trim().split(",")) {
            forwardedHeaderName = forwardedHeaderName.trim();
            if (hasText(forwardedHeaderName))
                result.add(forwardedHeaderName);
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oQuery.class;
    }

    private boolean hasHeadersToForward(N2oQuery.Selection[] selection) {
        if (selection == null) return true;
        return Arrays.stream(selection).anyMatch(this::hasHeadersToForward);
    }

    private boolean hasHeadersToForward(N2oQuery.Selection selection) {
        if (selection == null)
            return false;
        return selection.getInvocation() instanceof N2oGraphQlDataProvider || selection.getInvocation() instanceof N2oRestDataProvider;
    }
}
