package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения и записи виджета дерево
 */
public class TreeXmlIOv5Test {
    @Test
    public void testTreeXmlIOV5() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new TreeElementIOv5());

        assert tester.check("net/n2oapp/framework/config/io/widget/tree/testTreeWidgetIOv5.widget.xml");
    }
}
