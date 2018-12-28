package net.n2oapp.framework.api.reader;

import net.n2oapp.framework.api.factory.MetadataFactory;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.SourceInfo;

public interface SourceLoaderFactory extends MetadataFactory<SourceLoader> {

    <S extends SourceMetadata, I extends SourceInfo> S read(I info, String params);

}
