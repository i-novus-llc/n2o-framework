package net.n2oapp.framework.config.io.control;

import net.n2oapp.context.CacheTemplateByMapMock;
import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.framework.api.metadata.control.plain.N2oHtml;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.mock;

/**
 * Тестирование чтения и записи контрола Html из/в xml-файла
 */
public class N2oHtmlXmlIOTest extends N2oStandardControlReaderTestBase {

    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        StaticSpringContext staticSpringContext = new StaticSpringContext();
        staticSpringContext.setApplicationContext(applicationContext);
        staticSpringContext.setCacheTemplate(new CacheTemplateByMapMock());
    }

    @Test
    public void testHtmlXmlIO(){
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister());

        assert tester.check("net/n2oapp/framework/config/io/control/testHtmlReader.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 1);
                    N2oHtml text = (N2oHtml) fieldSet.getItems()[0];
                    assertStandardAttribute(text);
                    assert text.getHeight().equals("100px");
                });
    }
}