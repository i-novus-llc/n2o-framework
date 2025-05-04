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

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DynamicConfigReaderTest {
    
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
        assertEquals("sec?role", metadata.getId());
        cache.clear();
        metadata = reader.load(new JavaInfo("sec", N2oObject.class), "role");
        assertEquals("sec?role", metadata.getId());
        //проверяем кэширование
        List ids = cache.stream().map(SourceMetadata::getId).toList();
        assertEquals(2, ids.size());
        assertTrue(ids.contains("sec?role"));
        cache.clear();
        //проверяем чтение
        metadata = reader.load(new JavaInfo("amb", N2oPage.class), "page1");
        //проверяем кэширование
        ids = cache.stream().map(SourceMetadata::getId).toList();
        assertEquals(3, ids.size());
        assertTrue(ids.contains("amb?page1"));
        assertTrue(ids.contains("amb?page2"));
        assertTrue(ids.contains("amb?object1"));

        assertEquals("amb?page1", metadata.getId());
        metadata = reader.load(new JavaInfo("amb", N2oPage.class), "page2");
        assertEquals("amb?page2", metadata.getId());
        metadata = reader.load(new JavaInfo("amb", N2oObject.class), "object1");
        assertEquals("amb?object1", metadata.getId());
    }
    
    private static <T extends SourceMetadata> T setId(T metadata, String id) {
        metadata.setId(id);
        return metadata;
    }
}
