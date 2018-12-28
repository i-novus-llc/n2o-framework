package net.n2oapp.framework.config.audit.git.util.mock;

import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.InfoConstructor;
import net.n2oapp.framework.config.register.scanner.XmlInfoScanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.config.audit.git.util.N2oGitTestUtil.resolveURI;

/**
 * @author rgalina
 * @since 22.02.2017
 */
public class XmlInfoScannerMock extends XmlInfoScanner {

    private Map<String, String> mapUri;

    public XmlInfoScannerMock(String pattern, SourceTypeRegister metaModelRegister) {
        super(pattern, metaModelRegister);
        mapUri = new HashMap<>();
        mapUri.put("object/object1.object.xml", resolveURI("object/object1.object.xml"));
        mapUri.put("object/object2.object.xml", resolveURI("object/object2.object.xml"));
        mapUri.put("object/object3.object.xml", resolveURI("object/object3.object.xml"));
        mapUri.put("object4.object.xml", resolveURI("object4.object.xml"));
        mapUri.put("object/object5.object.xml", resolveURI("object/object5.object.xml"));
        mapUri.put("page/page1.page.xml", resolveURI("page/page1.page.xml"));
        mapUri.put("page/page2.page.xml", resolveURI("page/page2.page.xml"));
        mapUri.put("page/page3.page.xml", resolveURI("page/page3.page.xml"));
        mapUri.put("page/page4.page.xml", resolveURI("page/page4.page.xml"));
    }

    @Override
    public List<InfoConstructor> scan() {
        List<InfoConstructor> infos = new ArrayList<>();
        InfoConstructor in = new InfoConstructor("object1", getSourceTypeRegister().get(N2oObject.class));
        in.setLocalPath("object/object1.object.xml");
        in.setUri(mapUri.get(in.getLocalPath()));
        infos.add(in);
        InfoConstructor in2 = new InfoConstructor("object2", getSourceTypeRegister().get(N2oObject.class));
        in2.setLocalPath("object/object2.object.xml");
        in2.setUri(mapUri.get(in2.getLocalPath()));
        infos.add(in2);
        InfoConstructor in3 = new InfoConstructor("object3", getSourceTypeRegister().get(N2oObject.class));
        in3.setLocalPath("object/object3.object.xml");
        in3.setUri(mapUri.get(in3.getLocalPath()));
        infos.add(in3);
        InfoConstructor in4 = new InfoConstructor("object4", getSourceTypeRegister().get(N2oObject.class));
        in4.setLocalPath("object4.object.xml");
        in4.setUri(mapUri.get(in4.getLocalPath()));
        mapUri.put("object/object4.object.xml", resolveURI("object/object4.object.xml"));
        infos.add(in4);
        return infos;
    }

}
