package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.control.list.ListType;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.Select;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование компиляции компонента выбора из выпадающего списка
 */
public class SelectCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.properties("n2o.api.control.list.cache=true");
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV3IOPack(), new N2oQueriesPack());
        builder.compilers(new SelectCompiler());
    }

    @Test
    void testSelect() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testSelect.page.xml",
                "net/n2oapp/framework/config/metadata/compile/control/test.query.xml")
                .get(new PageContext("testSelect"));
        Form form = (Form) page.getWidget();
        Field field = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        Select select = (Select) ((StandardField) field).getControl();
        assertThat(select.getSrc(), is("N2OSelect"));
        assertThat(select.getType(), is(ListType.CHECKBOXES));
        assertThat(select.getClosePopupOnSelect(), is(false));
        assertThat(select.getCleanable(), is(false));
        assertThat(select.getSelectFormatOne(), is("{size} объект"));
        assertThat(select.getSelectFormatFew(), is("{size} объекта"));
        assertThat(select.getSelectFormatMany(), is("{size} объектов"));
        assertThat(select.getDescriptionFieldId(), is("descFieldId"));
        assertThat(select.getPlaceholder(), is("Введите"));

        field = form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        select = (Select) ((StandardField) field).getControl();
        assertThat(select.getType(), is(ListType.CHECKBOXES));
        assertThat(select.getSelectFormat(), is("Логичных примеров {size} шт"));

        field = form.getComponent().getFieldsets().get(0).getRows().get(2).getCols().get(0).getFields().get(0);
        select = (Select) ((StandardField) field).getControl();
        assertThat(select.getType(), is(ListType.SINGLE));
        assertThat(select.getClosePopupOnSelect(), is(true));
        assertThat(select.getCleanable(), is(true));
        assertThat(select.getData().size(), is(2));
        assertThat(select.getData().get(0).get("id"), is("type1"));
        assertThat(select.getData().get(0).get("name"), is("A"));
        assertThat(select.getData().get(1).get("id"), is("type2"));
        assertThat(select.getData().get(1).get("name"), is("B"));

        field = form.getComponent().getFieldsets().get(0).getRows().get(3).getCols().get(0).getFields().get(0);
        select = (Select) ((StandardField) field).getControl();
        assertThat(select.getCaching(), is(true));

        field = form.getComponent().getFieldsets().get(0).getRows().get(4).getCols().get(0).getFields().get(0);
        select = (Select) ((StandardField) field).getControl();
        assertThat(select.getType(), is(ListType.SINGLE));
        assertThat(select.getDatasource(), is("testSelect_test"));
        assertThat(select.getData(), nullValue());
    }

    @Test
    void testSelectCheckPrefilter() {
        assertThrows(
                N2oException.class,
                () -> compile("net/n2oapp/framework/config/metadata/compile/control/testSelectPrefilter.page.xml",
                        "net/n2oapp/framework/config/metadata/compile/control/test.query.xml")
                        .get(new PageContext("testSelectPrefilter"))
                );
    }
}
