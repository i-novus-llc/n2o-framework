package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.storage.Node;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static net.n2oapp.framework.config.register.RegisterUtil.createXmlInfo;
import static net.n2oapp.framework.config.register.RegisterUtil.getIdAndPostfix;
import static net.n2oapp.framework.config.register.storage.Node.byDirectory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование утилитного класса для работы с регистром
 */
class RegisterUtilTest {

    @Test
    void testGetIdAndPostfix() {
        String[] idAndPostfix = getIdAndPostfix("/opt/n2o/conf/objects/some/some.object.xml");
        assertEquals("some", idAndPostfix[0]);
        assertEquals("object", idAndPostfix[1]);
        assertThrows(IllegalStateException.class, () -> getIdAndPostfix("/opt/n2o/conf/objects/some/some.object.page.xml"));
    }

    @Test
    void testCyrillic() {
        String[] res = getIdAndPostfix("file:/opt/rmis/rmis-conf/report/form/report_002_О_у_10_journal_of_arms_221.widget.xml");
        assertEquals(3, res.length);
    }

    @Test
    void testCreateXmlInfo() {
        SourceTypeRegister metaModelRegister = new N2oSourceTypeRegister();
        metaModelRegister.addAll(List.of(new MetaType("object", N2oObject.class)));
        Node node = byDirectory(new File("/opt/n2o/conf/some/objects/some.object.xml"), "/opt/n2o/conf");
        InfoConstructor info = createXmlInfo(node, metaModelRegister);
        assertEquals("some", info.getId());
        assertEquals(OriginEnum.XML, info.getOrigin());
    }

    @Test
    void testInfoGetDirectory() {
        SourceTypeRegister metaModelRegister = new N2oSourceTypeRegister();
        metaModelRegister.addAll(List.of(new MetaType("object", N2oObject.class)));
        InfoConstructor info = new InfoConstructor();
        info.setLocalPath("/some/objects/some.object.xml");
        info.setConfigId(new ConfigId("some", metaModelRegister.get(N2oObject.class)));
        info.setOrigin(OriginEnum.XML);
        assertEquals("/some/objects", info.getDirectory());

        info = new InfoConstructor();
        info.setLocalPath("some.object.xml");
        info.setConfigId(new ConfigId("some", metaModelRegister.get(N2oObject.class)));
        info.setOrigin(OriginEnum.XML);
        assertEquals("", info.getDirectory());
    }
}