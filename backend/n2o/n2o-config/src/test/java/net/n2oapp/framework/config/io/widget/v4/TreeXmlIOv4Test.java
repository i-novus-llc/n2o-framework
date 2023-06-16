package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения и записи виджета дерево
 */
public class TreeXmlIOv4Test {
    
    @Test
    void testTreeXmlIOV4() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TreeElementIOv4());

        assert tester.check("net/n2oapp/framework/config/io/widget/tree/testTreeWidgetIOv4.widget.xml");
    }
}
