package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.config.register.dynamic.JavaSourceLoader;
import net.n2oapp.framework.config.register.dynamic.N2oDynamicMetadataProviderFactory;
import net.n2oapp.framework.config.register.mock.TestDynamicMetadataProvider;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class DynamicConfigReaderTest {
    
    @Test
    void test() {
        List<SourceMetadata> cache = new ArrayList<>();
        N2oDynamicMetadataProviderFactory providerFactory = new N2oDynamicMetadataProviderFactory();
        providerFactory.add(
                new TestDynamicMetadataProvider("sec", asList(
                        setId(new N2oStandardPage(), "sec?role"),
                        setId(new N2oObject(), "sec?role"))),
                new TestDynamicMetadataProvider("amb", asList(
                        setId(new N2oStandardPage(), "amb?page1"),
                        setId(new N2oStandardPage(), "amb?page2"),
                        setId(new N2oObject(), "amb?object1"))));
        JavaSourceLoader reader = new JavaSourceLoader(providerFactory, cache::add);
        //проверяем чтение
        SourceMetadata metadata = reader.load(new JavaInfo("sec", N2oPage.class), "role");
        assert metadata.getId().equals("sec?role");
        cache.clear();
        metadata = reader.load(new JavaInfo("sec", N2oObject.class), "role");
        assert metadata.getId().equals("sec?role");
        //проверяем кэширование
        List ids = cache.stream().map(SourceMetadata::getId).collect(Collectors.toList());
        assert ids.size() == 2;
        assert ids.contains("sec?role");
        cache.clear();
        //проверяем чтение
        metadata = reader.load(new JavaInfo("amb", N2oPage.class), "page1");
        //проверяем кэширование
        ids = cache.stream().map(SourceMetadata::getId).collect(Collectors.toList());
        assert ids.size() == 3;
        assert ids.contains("amb?page1");
        assert ids.contains("amb?page2");
        assert ids.contains("amb?object1");

        assert metadata.getId().equals("amb?page1");
        metadata = reader.load(new JavaInfo("amb", N2oPage.class), "page2");
        assert metadata.getId().equals("amb?page2");
        metadata = reader.load(new JavaInfo("amb", N2oObject.class), "object1");
        assert metadata.getId().equals("amb?object1");
    }
    
    private static <T extends SourceMetadata> T setId(T metadata, String id) {
        metadata.setId(id);
        return metadata;
    }
}
