package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.object.ObjectElementIOv1;
import net.n2oapp.framework.config.reader.page.PageXmlReaderV1;
import net.n2oapp.framework.config.reader.query.QueryElementReaderV3;
import net.n2oapp.framework.config.selective.CompileInfo;

public class StubsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder builder) {
        builder.ios(new ObjectElementIOv1())
                .packs(new N2oInvocationV2ReadersPack())
                .readers(new QueryElementReaderV3(), new PageXmlReaderV1())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml"));
    }
}
