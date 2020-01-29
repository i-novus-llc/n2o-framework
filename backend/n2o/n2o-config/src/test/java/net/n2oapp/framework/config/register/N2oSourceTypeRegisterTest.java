package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBasePage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.reader.MetaTypeNotFoundException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class N2oSourceTypeRegisterTest {

    @Test
    public void get() {
        SourceTypeRegister register = new N2oSourceTypeRegister();
        MetaType metaType = new MetaType("widget", N2oWidget.class);
        register.add(metaType);

        //получение по типу
        assertThat(register.get("widget"), is(metaType));

        //получение по классу
        assertThat(register.get(N2oWidget.class), is(metaType));

        //получение по дочернему классу
        assertThat(register.get(N2oForm.class), is(metaType));

        //получение по несуществующему типу
        try {
            register.get("test");
            Assert.fail();
        } catch (MetaTypeNotFoundException ignored) {
        }

        //получение по родительскому классу
        try {
            register.get(N2oMetadata.class);
            Assert.fail();
        } catch (MetaTypeNotFoundException ignored) {
        }

        //получение по недочернему классу
        try {
            register.get(N2oBasePage.class);
            Assert.fail();
        } catch (MetaTypeNotFoundException ignored) {
        }
    }

    @Test
    public void cleanAll() {
        SourceTypeRegister register = new N2oSourceTypeRegister();
        register.addAll(Arrays.asList(new MetaType("widget", N2oWidget.class), new MetaType("page", N2oBasePage.class)));
        assertThat(register.get("widget"), notNullValue());
        assertThat(register.get("page"), notNullValue());

        register.clearAll();
        try {
            register.get("widget");
            Assert.fail();
        } catch (MetaTypeNotFoundException ignored) {
        }
        try {
            register.get(N2oBasePage.class);
            Assert.fail();
        } catch (MetaTypeNotFoundException ignored) {
        }

    }
}
