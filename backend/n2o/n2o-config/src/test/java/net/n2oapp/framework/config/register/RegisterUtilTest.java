package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.storage.Node;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Тестирование утилитного класса для работы с регистром
 */
public class RegisterUtilTest {
    
    @Test
    void testGetIdAndPostfix() throws Exception {
        String[] idAndPostfix = RegisterUtil.getIdAndPostfix("/opt/n2o/conf/objects/some/some.object.xml");
        assert idAndPostfix[0].equals("some");
        assert idAndPostfix[1].equals("object");
        try {
            RegisterUtil.getIdAndPostfix("/opt/n2o/conf/objects/some/some.object.page.xml");
            assert false;
        } catch (IllegalStateException e) {
            assert true;
        }

    }

    @Test
    void testCyrillic() throws Exception {
        String[] res = RegisterUtil.getIdAndPostfix("file:/opt/rmis/rmis-conf/report/form/report_002_О_у_10_journal_of_arms_221.widget.xml");
        assert res.length == 3;
    }

    @Test
    void testCreateXmlInfo() throws Exception {
        SourceTypeRegister metaModelRegister = new N2oSourceTypeRegister();
        metaModelRegister.addAll(Arrays.asList(new MetaType("object", N2oObject.class)));
        Node node = Node.byDirectory(new File("/opt/n2o/conf/some/objects/some.object.xml"), "/opt/n2o/conf");
        InfoConstructor info = RegisterUtil.createXmlInfo(node, metaModelRegister);
        assert info.getId().equals("some");
        assert info.getOrigin().equals(Origin.xml);
    }

    @Test
    void testInfoGetDirectory() throws Exception {
        SourceTypeRegister metaModelRegister = new N2oSourceTypeRegister();
        metaModelRegister.addAll(Arrays.asList(new MetaType("object", N2oObject.class)));
        InfoConstructor info = new InfoConstructor();
        info.setLocalPath("/some/objects/some.object.xml");
        info.setConfigId(new ConfigId("some", metaModelRegister.get(N2oObject.class)));
        info.setOrigin(Origin.xml);
        assert info.getDirectory().equals("/some/objects");

        info = new InfoConstructor();
        info.setLocalPath("some.object.xml");
        info.setConfigId(new ConfigId("some", metaModelRegister.get(N2oObject.class)));
        info.setOrigin(Origin.xml);
        assert info.getDirectory().equals("");
    }

}
