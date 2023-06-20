package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.config.reader.ReferentialIntegrityViolationException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class N2oMetadataRegisterTest {

    @Test
    void contains() {
        MetadataRegister register = new N2oMetadataRegister();
        SourceInfo info = new XmlInfo("test", N2oWidget.class, "test.widget.xml");
        register.add(info);

        //существование идентичного
        assertThat(register.contains("test", N2oWidget.class), is(true));

        //существование в верхнем регистре
        assertThat(register.contains("TEST", N2oWidget.class), is(true));

        //существование дочернего класса
        assertThat(register.contains("test", N2oForm.class), is(true));

        //существование любого дочернего класса
        assertThat(register.contains("test", N2oTable.class), is(true));

        //существование родительского класса
        assertThat(register.contains("test", N2oMetadata.class), is(false));

        //существование не дочернего класса
        assertThat(register.contains("test", N2oPage.class), is(false));

        //существование отсутствующего идентификатора
        assertThat(register.contains("test2", N2oWidget.class), is(false));

        //существование динамического
        assertThat(register.contains("test?param", N2oWidget.class), is(true));
    }

    @Test
    void get() {
        MetadataRegister register = new N2oMetadataRegister();
        SourceInfo info = new XmlInfo("test", N2oWidget.class, "test.widget.xml");
        register.add(info);

        //получение идентичного
        assertThat(register.get("test", N2oWidget.class), is(info));

        //получение дочернего класса
        assertThat(register.get("test", N2oForm.class), is(info));

        //получение динамического
        assertThat(register.get("test?param", N2oForm.class), is(info));

        //получение несуществующего
        try {
            register.get("test2", N2oWidget.class);
            fail();
        } catch (ReferentialIntegrityViolationException ignore) { }
    }

    @Test
    void remove() {
        MetadataRegister register = new N2oMetadataRegister();
        SourceInfo info = new XmlInfo("test", N2oWidget.class, "test.widget.xml");

        //удаление дочернего класса
        register.add(info);
        assertThat(register.contains("test", N2oWidget.class), is(true));
        register.remove("test", N2oForm.class);
        assertThat(register.contains("test", N2oWidget.class), is(false));

        //удаление в верхнем регистре
        register.add(info);
        assertThat(register.contains("test", N2oWidget.class), is(true));
        register.remove("TEST", N2oWidget.class);
        assertThat(register.contains("test", N2oWidget.class), is(false));

        //удаление несуществующего
        try {
            register.remove("test2", N2oWidget.class);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void cleanAll() {
        MetadataRegister register = new N2oMetadataRegister();
        register.add(new XmlInfo("test1", N2oWidget.class, "test1.widget.xml"));
        register.add(new XmlInfo("test2", N2oWidget.class, "test2.widget.xml"));
        assertThat(register.contains("test1", N2oWidget.class), is(true));
        assertThat(register.contains("test2", N2oWidget.class), is(true));

        register.clearAll();
        assertThat(register.contains("test1", N2oWidget.class), is(false));
        assertThat(register.contains("test2", N2oWidget.class), is(false));
    }
}
