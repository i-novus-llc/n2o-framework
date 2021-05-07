package net.n2oapp.framework.config.metadata.compile.query;

import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Валидатор метаданных запроса
 */
@Component
public class N2oQueryValidator extends TypedMetadataValidator<N2oQuery> {

    @Value("${n2o.config.java.mapping}")
    private String mapping;

    @Override
    public Class<N2oQuery> getSourceClass() {
        return N2oQuery.class;
    }

    @Override
    public void validate(N2oQuery n2oQuery, ValidateProcessor p) {
        if (mapping.equals("map")) {
            if (n2oQuery.getLists() != null)
                checkQuery(n2oQuery, n2oQuery.getLists(), p);
            if (n2oQuery.getCounts() != null)
                checkQuery(n2oQuery, n2oQuery.getCounts(), p);
            if (n2oQuery.getUniques() != null)
                checkQuery(n2oQuery, n2oQuery.getUniques(), p);
        }
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    private void checkQuery(N2oQuery n2oQuery, N2oQuery.Selection[] lists, ValidateProcessor p) {
        for (N2oQuery.Selection selection : lists)
            if (selection.getInvocation() instanceof N2oJavaDataProvider)
                checkJavaDataProvider(p, (N2oJavaDataProvider) selection.getInvocation(), n2oQuery.getId());
    }

    private void checkJavaDataProvider(ValidateProcessor p, N2oJavaDataProvider n2oJavaDataProvider, String queryId) {
        if (n2oJavaDataProvider.getArguments() != null) {
            for (Argument arg : n2oJavaDataProvider.getArguments()) {
                p.checkNotNull(arg.getName(),
                        String.format("В запросе %s для всех аргументов в java provider должно быть задано имя", queryId));
            }
        }
    }

}