package net.n2oapp.properties;

import net.n2oapp.properties.io.PropertiesInfoCollector;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * @author V. Alexeev.
 */
public class PropertiesInfoCollectorTest {

    @Test
    void test() {
        PropertiesInfoCollector infoCollector = new PropertiesInfoCollector("info.properties");
        Map<String, List<PropertiesInfoCollector.PropertyInfo>> propertyInfoMap = infoCollector.getPropertyInfoMap();
        assert propertyInfoMap.containsKey("");

        List<PropertiesInfoCollector.PropertyInfo> propertyInfos = propertyInfoMap.get("Группа пропертей");
        assert propertyInfos.size() == 2;
        assert propertyInfos.get(0).name == null;
        assert propertyInfos.get(0).description == null;
        assert propertyInfos.get(1).name.equals("Группа + название");

        propertyInfos = propertyInfoMap.get("");
        assert propertyInfos.size() == 7;
        assert propertyInfos.get(0).name == null;
        assert propertyInfos.get(0).description == null;

        assert propertyInfos.get(1).name.equals("Проперти с названием");
        assert propertyInfos.get(1).description == null;

        assert propertyInfos.get(2).name.equals("Проперти с названием2");
        assert propertyInfos.get(2).description.equals("комментарий1");

        assert propertyInfos.get(3).name.equals("Проперти с названием3");
        assert propertyInfos.get(3).description.equals("комментарий1 комментарий2");

        assert propertyInfos.get(4).name.equals("Проперти с названием3");
        assert propertyInfos.get(4).description.equals("комментарий1 комментарий2");

        assert propertyInfos.get(5).name.equals("название");
        assert propertyInfos.get(5).description == null;
    }

}
