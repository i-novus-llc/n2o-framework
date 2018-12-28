package net.n2oapp.framework.config.io.control;

import net.n2oapp.context.CacheTemplateByMapMock;
import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.framework.api.metadata.control.plain.N2oCheckbox;
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
 * Тестирование чтения и записи контрола checkbox
 */
public class N2oCheckboxXmlIOTest extends N2oStandardControlReaderTestBase {

    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        StaticSpringContext staticSpringContext = new StaticSpringContext();
        staticSpringContext.setApplicationContext(applicationContext);
        staticSpringContext.setCacheTemplate(new CacheTemplateByMapMock());
    }

    @Test
    public void testCheckboxXmlIO(){
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister());

        assert tester.check("net/n2oapp/framework/config/io/control/testCheckboxReader.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 1);
                    N2oCheckbox checkbox = (N2oCheckbox) fieldSet.getItems()[0];
                    assertStandardAttribute(checkbox);
                });
    }
}
