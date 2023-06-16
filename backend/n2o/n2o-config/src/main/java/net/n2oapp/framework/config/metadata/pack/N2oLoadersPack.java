package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.reader.XmlMetadataLoader;
import net.n2oapp.framework.config.register.dynamic.JavaSourceLoader;

/**
 * Стандартные считыватели метаданных
 */
public class N2oLoadersPack implements MetadataPack<N2oApplicationBuilder> {

    @Override
    public void build(N2oApplicationBuilder b) {
        b.loaders(new XmlMetadataLoader(b.getEnvironment().getNamespaceReaderFactory()),
                  new JavaSourceLoader(b.getEnvironment().getDynamicMetadataProviderFactory()));
    }

}
