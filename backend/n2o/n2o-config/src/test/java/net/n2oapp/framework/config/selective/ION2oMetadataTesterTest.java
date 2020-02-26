package net.n2oapp.framework.config.selective;

import net.n2oapp.context.CacheTemplateByMapMock;
import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.engine.factory.EngineNotFoundException;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetRow;
import net.n2oapp.framework.api.util.N2oTestUtil;
import net.n2oapp.framework.config.io.control.plain.InputTextIOv2;
import net.n2oapp.framework.config.io.fieldset.LineFieldsetElementIOv4;
import net.n2oapp.framework.config.io.fieldset.MultiFieldsetElementIOv4;
import net.n2oapp.framework.config.io.fieldset.RowElementIO4;
import net.n2oapp.framework.config.io.fieldset.SetFieldsetElementIOv4;
import net.n2oapp.framework.config.persister.control.N2oInputTextPersister;
import net.n2oapp.framework.config.reader.control.N2oInputTextXmlReaderV1;
import net.n2oapp.framework.config.selective.persister.SelectivePersister;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
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

    @Test
    public void testFieldSet1() {
        ION2oMetadataTester tester = new ION2oMetadataTester().addReader(new SelectiveReader()
                .addReader(new SetFieldsetElementIOv4())
                .addReader(new RowElementIO4())
                .addReader(new N2oInputTextXmlReaderV1()))
                .addPersister(new SelectivePersister()
                        .addPersister(new SetFieldsetElementIOv4())
                        .addPersister(new LineFieldsetElementIOv4())
                        .addPersister(new MultiFieldsetElementIOv4())
                        .addPersister(new N2oInputTextPersister())
                        .addPersister(new RowElementIO4()));
        tester.ios(new InputTextIOv2());

        assert tester.check("net/n2oapp/framework/config/selective/duplex/test1.fieldset.xml", (N2oFieldSet fieldSet) -> {
            assert ((N2oFieldsetRow) fieldSet.getItems()[0]).getItems().length == 1;
            assert ((N2oFieldsetRow) fieldSet.getItems()[1]).getItems().length == 4;
            assert ((N2oField) ((N2oFieldsetRow) fieldSet.getItems()[0]).getItems()[0]).getId().equals("test1");
        });
    }

    @Test
    public void testFieldSet2() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveReader().addReader(new SetFieldsetElementIOv4())
                        .addReader(new RowElementIO4()).addReader(new N2oInputTextXmlReaderV1()))
                .addPersister(new SelectivePersister().addPersister(new SetFieldsetElementIOv4())
                        .addPersister(new LineFieldsetElementIOv4())
                        .addPersister(new MultiFieldsetElementIOv4())
                        .addPersister(new N2oInputTextPersister()).addPersister(new RowElementIO4()))
                .ios(new InputTextIOv2());
        assert tester.check("net/n2oapp/framework/config/selective/duplex/test2.fieldset.xml", (N2oFieldSet fieldSet) -> {
            assert ((N2oFieldsetRow) fieldSet.getItems()[0]).getItems().length == 4;
            assert ((N2oField) fieldSet.getItems()[1]).getId().equals("test1");
        });
    }

}
