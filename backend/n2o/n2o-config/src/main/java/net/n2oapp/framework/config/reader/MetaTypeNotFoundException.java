package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceMetadata;

public class MetaTypeNotFoundException extends N2oException {

    public MetaTypeNotFoundException(String sourceType) {
        super("Source type [" + sourceType + "] hadn't been registered");
    }

    public MetaTypeNotFoundException(Class<? extends Source> sourceClass) {
        super("Source class [" + sourceClass + "] hadn't been registered");
    }
}
