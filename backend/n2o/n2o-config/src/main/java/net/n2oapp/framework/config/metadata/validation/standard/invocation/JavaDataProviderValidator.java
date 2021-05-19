package net.n2oapp.framework.config.metadata.validation.standard.invocation;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.config.metadata.compile.dataprovider.DataProviderScope;
import org.springframework.stereotype.Component;

/**
 * Валидатор java провайдера данных
 */
@Component
public class JavaDataProviderValidator extends TypedMetadataValidator<N2oJavaDataProvider> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oJavaDataProvider.class;
    }

    @Override
    public void validate(N2oJavaDataProvider provider, ValidateProcessor p) {
//        String mapping = p.

        if ("map".equals("mapping") && provider.getArguments() != null) {
            DataProviderScope scope = p.getScope(DataProviderScope.class);
            String message = scope.getQueryId() != null ? "В выборке " + scope.getQueryId() :
                    "В объекте " + scope.getObjectId();

            for (Argument arg : provider.getArguments())
                p.checkNotNull(arg.getName(),
                        String.format("%s для всех аргументов в <java> провайдере должен быть задан атрибут name", message));
        }
    }
}
