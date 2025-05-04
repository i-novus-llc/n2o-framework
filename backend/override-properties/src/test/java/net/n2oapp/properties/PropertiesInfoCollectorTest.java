package net.n2oapp.properties;

import net.n2oapp.properties.io.PropertiesInfoCollector;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author V. Alexeev.
 */
class PropertiesInfoCollectorTest {

    @Test
    void test() {
        PropertiesInfoCollector infoCollector = new PropertiesInfoCollector("info.properties");
        Map<String, List<PropertiesInfoCollector.PropertyInfo>> propertyInfoMap = infoCollector.getPropertyInfoMap();
        assertTrue(propertyInfoMap.containsKey(""));

        List<PropertiesInfoCollector.PropertyInfo> propertyInfos = propertyInfoMap.get("Группа пропертей");
        assertEquals(2, propertyInfos.size());
        assertNull(propertyInfos.get(0).name);
        assertNull(propertyInfos.get(0).description);
        assertEquals("Группа + название", propertyInfos.get(1).name);

        propertyInfos = propertyInfoMap.get("");
        assertEquals(7, propertyInfos.size());
        assertNull(propertyInfos.get(0).name);
        assertNull(propertyInfos.get(0).description);

        assertEquals("Проперти с названием", propertyInfos.get(1).name);
        assertNull(propertyInfos.get(1).description);

        assertEquals("Проперти с названием2", propertyInfos.get(2).name);
        assertEquals("комментарий1", propertyInfos.get(2).description);

        assertEquals("Проперти с названием3", propertyInfos.get(3).name);
        assertEquals("комментарий1 комментарий2", propertyInfos.get(3).description);

        assertEquals("Проперти с названием3", propertyInfos.get(4).name);
        assertEquals("комментарий1 комментарий2", propertyInfos.get(4).description);

        assertEquals("название", propertyInfos.get(5).name);
        assertNull(propertyInfos.get(5).description);
    }
}