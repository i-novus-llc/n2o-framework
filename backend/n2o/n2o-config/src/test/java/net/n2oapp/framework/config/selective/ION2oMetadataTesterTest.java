package net.n2oapp.framework.config.selective;

import net.n2oapp.context.CacheTemplateByMapMock;
import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.engine.factory.EngineNotFoundException;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.util.N2oTestUtil;
import net.n2oapp.framework.config.persister.control.N2oInputTextPersister;
import net.n2oapp.framework.config.reader.control.N2oInputTextXmlReaderV1;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

public class ION2oMetadataTesterTest {
    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
        StaticSpringContext staticSpringContext = new StaticSpringContext();
        staticSpringContext.setApplicationContext(applicationContext);
        staticSpringContext.setCacheTemplate(new CacheTemplateByMapMock());
    }

    @Test
    public void testSimple() {
        final ION2oMetadataTester tester = new ION2oMetadataTester().addReader(new N2oInputTextXmlReaderV1());
        // нету персистера
        N2oTestUtil.assertOnException(() -> tester.check("net/n2oapp/framework/config/selective/duplex/inputText.xml"), EngineNotFoundException.class);
        // добавим персистер
        tester.addPersister(new N2oInputTextPersister());
        tester.check("net/n2oapp/framework/config/selective/duplex/inputText.xml");
        tester.check("net/n2oapp/framework/config/selective/duplex/inputText.xml", (N2oStandardField n2o) -> {
            assert n2o.getId().equals("id");
            assert n2o.getLabel().equals("label");
            assert n2o.getDefaultValue() == null;
        });
    }

}
