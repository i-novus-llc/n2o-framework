package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.ReadersBuilder;
import net.n2oapp.framework.config.reader.invocation.JavaInvocationReaderV2;
import net.n2oapp.framework.config.reader.invocation.RestInvocationReaderV2;

/**
 * Набор для сборки провайдеров данных n2o-invocations-2.0
 */
@Deprecated //use data-providers-1.0
public class N2oInvocationV2ReadersPack implements MetadataPack<ReadersBuilder> {
    @Override
    public void build(ReadersBuilder b) {
        b.readers(new JavaInvocationReaderV2(),
                new RestInvocationReaderV2());
    }
}
