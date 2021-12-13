package net.n2oapp.framework.config.metadata.validation.standard.invocation;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
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
    public void validate(N2oJavaDataProvider provider, SourceProcessor p) {
    }
}
