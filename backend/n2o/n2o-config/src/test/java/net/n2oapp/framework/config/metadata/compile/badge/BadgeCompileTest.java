package net.n2oapp.framework.config.metadata.compile.badge;

import net.n2oapp.framework.api.metadata.meta.badge.Badge;
import net.n2oapp.framework.api.metadata.meta.badge.Position;
import net.n2oapp.framework.api.metadata.meta.badge.Shape;
import net.n2oapp.framework.api.metadata.meta.control.ListControl;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Тестирование компиляции значков
 */
public class BadgeCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oActionsPack(), new N2oControlsPack(), new N2oFieldSetsPack());
    }

    @Test
    public void testControls() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/badge/testControls.page.xml")
                .get(new PageContext("testControls"));

        List<FieldSet.Row> rows = (((Form) page.getWidget())).getComponent().getFieldsets().get(0).getRows();
        Badge badge = ((ListControl) ((StandardField) rows.get(0).getCols().get(0).getFields().get(0)).getControl()).getBadge();

        assertThat(badge.getFieldId(), is("bfi1"));
        assertThat(badge.getPosition(), is(Position.left));
        assertThat(badge.getShape(), is(Shape.rounded));
        assertThat(badge.getColorFieldId(), is("bcfi1"));
        assertThat(badge.getImageFieldId(), is("bifi1"));
        assertThat(badge.getImagePosition(), is(Position.right));
        assertThat(badge.getImageShape(), is(Shape.square));

        badge = ((ListControl) ((StandardField) rows.get(1).getCols().get(0).getFields().get(0)).getControl()).getBadge();
        assertThat(badge.getFieldId(), nullValue());
        assertThat(badge.getPosition(), is(Position.right));
        assertThat(badge.getShape(), is(Shape.square));
        assertThat(badge.getColorFieldId(), nullValue());
        assertThat(badge.getImageFieldId(), is("bifi2"));
        assertThat(badge.getImagePosition(), is(Position.left));
        assertThat(badge.getImageShape(), is(Shape.circle));

        badge = ((ListControl) ((StandardField) rows.get(2).getCols().get(0).getFields().get(0)).getControl()).getBadge();
        assertThat(badge, nullValue());
    }

}
