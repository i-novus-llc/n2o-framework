package net.n2oapp.framework.config;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.register.route.RouteInfo;
import net.n2oapp.framework.config.io.page.v3.SimplePageElementIOv3;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.StandardPageCompiler;
import net.n2oapp.framework.config.reader.XmlMetadataLoader;
import net.n2oapp.framework.config.register.XmlInfo;
import net.n2oapp.framework.config.register.scanner.MockInfoScanner;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;

public class N2oApplicationBuilderTest {
    @Test
    @Ignore
    public void test() {

        ReadCompileBindTerminalPipeline pipeline = new N2oApplicationBuilder()
                .scanners(new MockInfoScanner())
                .loaders(new XmlMetadataLoader(new ReaderFactoryByMap().register(new SimplePageElementIOv3())))
                .compilers( new StandardPageCompiler())
                .sources(new XmlInfo("test", N2oPage.class, "classpath:", "net/n2oapp/framework/config/test.page.xml"))
                .routes(new RouteInfo("/test", new PageContext("test")))
                .propertySources("application-test.properties")
                .scan()
                .read()
                .compile()
                .bind();

        Compiled compiled = new N2oApplicationBuilder().compile().copy().cache().bind().get(null, null, null);
        InputStream json = new N2oApplicationBuilder().read().cache().validate().serialize().get("test", N2oPage.class);
        N2oPage source = new N2oApplicationBuilder().read().merge().get("test", N2oPage.class);
        InputStream xml = new N2oApplicationBuilder().read().transform().persist().get("test", N2oPage.class);
        N2oPage source2 = new N2oApplicationBuilder().deserialize().validate().get(null, N2oPage.class);
        InputStream xml2 = new N2oApplicationBuilder().deserialize().persist().get(json, N2oPage.class);
    }
}
