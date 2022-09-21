package net.n2oapp.framework.config.metadata.serialize;

import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.api.metadata.jackson.ComponentTypeResolver;
import net.n2oapp.framework.api.metadata.jackson.SingletonTypeIdHandlerInstantiator;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllIOPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.XmlIOReader;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SerializeDeserializeTest extends SourceCompileTestBase {
    XmlIOReader reader;
    boolean print = true;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack());
        builder.scanComponentTypes("net.n2oapp.framework");
        builder.componentTypes(N2oPage.class);
        SingletonTypeIdHandlerInstantiator instantiator = new SingletonTypeIdHandlerInstantiator();
        ComponentTypeResolver typeIdResolver = new ComponentTypeResolver();
        typeIdResolver.setRegister(builder.getEnvironment().getComponentTypeRegister());
        instantiator.addTypeIdResolver(ComponentTypeResolver.class, typeIdResolver);
        reader = new XmlIOReader();
        reader.packs(new N2oAllIOPack());
    }

    @Test
    public void serializeSimplePage3Table() throws IOException {
        check("net/n2oapp/framework/config/metadata/serialize/serializeSimplePage3Table.page.xml", "serializeSimplePage3Table", N2oSimplePage.class);
    }

    @Test
    public void serializeSimplePage4Table() throws IOException {
        check("net/n2oapp/framework/config/metadata/serialize/serializeSimplePage4Table.page.xml", "serializeSimplePage4Table", N2oSimplePage.class);
    }

    @Test
    public void serializePage4() throws IOException {
        check("net/n2oapp/framework/config/metadata/serialize/serializePage4.page.xml", "serializePage4", N2oStandardPage.class);
    }

    @Test
    public void serializeSimplePage4Form() throws IOException {
        check("net/n2oapp/framework/config/metadata/serialize/simplePage4Form.page.xml", "simplePage4Form", N2oSimplePage.class);
    }

    private void check(String xml, String id, Class<? extends N2oPage> sourceClass) throws IOException {
        InputStream json = read(xml).serialize().get(id, sourceClass);
        if (print) {
            InputStream jsonForPrint = read(xml).serialize().get(id, sourceClass);
            System.out.println("Json:");
            System.out.println(IOUtils.toString(jsonForPrint, StandardCharsets.UTF_8));
        }
        N2oPage deserializedPage = builder.deserialize().get(json, N2oPage.class);
        check(xml, deserializedPage);
    }

    private void check(String xml, N2oPage deserializedPage) {
        reader.persistAndCompareWithSample(deserializedPage, FileSystemUtil.getContentFromResource(new ClassPathResource(xml)));
    }


}