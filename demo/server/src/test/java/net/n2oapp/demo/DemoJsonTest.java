package net.n2oapp.demo;

import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.register.route.RouteInfo;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.reader.XmlMetadataLoader;
import net.n2oapp.framework.config.register.scanner.XmlInfoScanner;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;

import java.util.HashMap;
import java.util.Map;

/**
 * Тест синхронизации json и xml метаданных демо стенда
 */
public class DemoJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        tester.setPipelineFunc((p) -> p.read().merge().transform().cache().copy().compile().transform().cache().copy().bind());
        tester.setPrintJsonOnFail(true);
    }

    @Override
    protected void configure(N2oApplicationBuilder b) {
        super.configure(b);
        b.loaders(new XmlMetadataLoader(b.getEnvironment().getNamespaceReaderFactory()));
        b.packs(new N2oAllPagesPack(), new N2oAllDataPack(), new N2oHeaderPack());
        b.scanners(new XmlInfoScanner());
        b.routes(new RouteInfo("/proto", new PageContext("ProtoPage", "/proto")));
        b.scan();
    }

    @Test
    public void proto() {
        check(new FileSystemResource("../../frontend/demo/server/json/newProto.json"))
                .assertEquals("/proto", Page.class);
    }

    @Test
    public void proto_patients_create() {
        check(new FileSystemResource("../../frontend/demo/server/json/proto_patients_create.json"))
                .assertEquals("/proto/create", Page.class);
    }

    @Test
    public void proto_patients_update() {
        check(new FileSystemResource("../../frontend/demo/server/json/proto_patients_update.json"))
                .assertEquals("/proto/5607677/update", Page.class);
    }

    @Test
    public void proto_patients_create2() {
        check(new FileSystemResource("../../frontend/demo/server/json/proto_patients_create2.json"))
                .assertEquals("/proto/create2", Page.class);
    }

    @Test
    public void proto_patients_update2() {
        Map<String, String[]> data = new HashMap<>();
        data.put("id", new String[]{ "5607677" });
        check(new FileSystemResource("../../frontend/demo/server/json/proto_patients_update2.json"))
                .assertEquals("/proto/5607677/update2", Page.class, data);
    }
}
