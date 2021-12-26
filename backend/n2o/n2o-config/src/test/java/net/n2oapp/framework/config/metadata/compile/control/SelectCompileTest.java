package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.control.list.ListType;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.Select;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oControlsV2IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции компонента выбора из выпадающего списка
 */
public class SelectCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV2IOPack());
        builder.compilers(new SelectCompiler());
    }

    @Test
    public void testSelect() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testSelect.page.xml")
                .get(new PageContext("testSelect"));
        Form form = (Form) page.getWidget();
        Field field = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        Select select = (Select) ((StandardField) field).getControl();
        assertThat(select.getSrc(), is("N2OSelect"));
        assertThat(select.getType(), is(ListType.checkboxes));
        assertThat(select.getClosePopupOnSelect(), is(false));
        assertThat(select.getCleanable(), is(false));
        assertThat(select.getSelectFormatOne(), is("{size} объект"));
        assertThat(select.getSelectFormatFew(), is("{size} объекта"));
        assertThat(select.getSelectFormatMany(), is("{size} объектов"));
        assertThat(select.getDescriptionFieldId(), is("descFieldId"));

        field = form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        select = (Select) ((StandardField) field).getControl();
        assertThat(select.getType(), is(ListType.checkboxes));
        assertThat(select.getSelectFormat(), is("Логичных примеров {size} шт"));

        field = form.getComponent().getFieldsets().get(0).getRows().get(2).getCols().get(0).getFields().get(0);
        select = (Select) ((StandardField) field).getControl();
        assertThat(select.getType(), is(ListType.single));
        assertThat(select.getClosePopupOnSelect(), is(true));
        assertThat(select.getCleanable(), is(true));
        assertThat(select.getData().size(), is(2));
        assertThat(select.getData().get(0).get("id"), is("type1"));
        assertThat(select.getData().get(0).get("name"), is("A"));
        assertThat(select.getData().get(1).get("id"), is("type2"));
        assertThat(select.getData().get(1).get("name"), is("B"));
    }
}
