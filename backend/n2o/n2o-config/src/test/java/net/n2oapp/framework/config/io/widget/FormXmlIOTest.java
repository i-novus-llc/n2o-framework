package net.n2oapp.framework.config.io.widget;


import net.n2oapp.framework.config.io.fieldset.*;
import net.n2oapp.framework.config.io.widget.form.FormElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Тестирование чтения и записи формы из/в xml-файла
 */
public class FormXmlIOTest {

    @Test
    public void testFormXmlIOV4() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader()
                        .addControlReader()
                        .addReader(new SetFieldsetElementIOv4())
                        .addReader(new LineFieldsetElementIOv4())
                        .addReader(new PanelFieldsetElementIOv4())
                        .addReader(new RowElementIO4())
                        .addReader(new ColElementIO4())
                        .addReader(new FormElementIOV4()))
                .addPersister(new SelectiveStandardPersister()
                        .addPersister(new SetFieldsetElementIOv4())
                        .addPersister(new LineFieldsetElementIOv4())
                        .addPersister(new PanelFieldsetElementIOv4())
                        .addPersister(new RowElementIO4())
                        .addControlPersister()
                        .addPersister(new ColElementIO4())
                        .addPersister(new FormElementIOV4()));

        assert tester.check("net/n2oapp/framework/config/io/widget/form/testFormWidgetIOv4.widget.xml");
    }


}
