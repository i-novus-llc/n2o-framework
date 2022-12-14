package net.n2oapp.framework.config.metadata.compile.badge;

import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.badge.Badge;
import net.n2oapp.framework.api.metadata.meta.badge.Position;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.api.metadata.meta.control.ListControl;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.Tree;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.form.FormWidgetComponent;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
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
                new N2oActionsPack(), new N2oControlsPack(), new N2oFieldSetsPack(),
                new N2oApplicationPack());
    }

    @Test
    public void testControls() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/badge/testControls.page.xml")
                .get(new PageContext("testControls"));

        List<FieldSet.Row> rows = (((Form) page.getWidget())).getComponent().getFieldsets().get(0).getRows();
        Badge badge = ((ListControl) ((StandardField) rows.get(0).getCols().get(0).getFields().get(0)).getControl()).getBadge();

        assertThat(badge.getFieldId(), is("bfi1"));
        assertThat(badge.getPosition(), is(Position.LEFT));
        assertThat(badge.getShape(), is(ShapeType.rounded));
        assertThat(badge.getColorFieldId(), is("bcfi1"));
        assertThat(badge.getImageFieldId(), is("bifi1"));
        assertThat(badge.getImagePosition(), is(Position.RIGHT));
        assertThat(badge.getImageShape(), is(ShapeType.square));
        assertThat(badge.getText(), nullValue());
        assertThat(badge.getColor(), nullValue());
        assertThat(badge.getImage(), nullValue());

        badge = ((ListControl) ((StandardField) rows.get(1).getCols().get(0).getFields().get(0)).getControl()).getBadge();
        assertThat(badge.getFieldId(), nullValue());
        assertThat(badge.getPosition(), is(Position.RIGHT));
        assertThat(badge.getShape(), is(ShapeType.square));
        assertThat(badge.getColorFieldId(), nullValue());
        assertThat(badge.getImageFieldId(), is("bifi2"));
        assertThat(badge.getImagePosition(), is(Position.LEFT));
        assertThat(badge.getImageShape(), is(ShapeType.circle));
        assertThat(badge.getText(), nullValue());
        assertThat(badge.getColor(), nullValue());
        assertThat(badge.getImage(), nullValue());

        badge = ((ListControl) ((StandardField) rows.get(2).getCols().get(0).getFields().get(0)).getControl()).getBadge();
        assertThat(badge, nullValue());
    }

    @Test
    public void testWidgets() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/badge/testWidgets.page.xml")
                .get(new PageContext("testWidgets"));

        Badge badge = ((Tree) page.getRegions().get("single").get(0).getContent().get(0)).getBadge();
        assertThat(badge.getFieldId(), is("bfi1"));
        assertThat(badge.getPosition(), is(Position.LEFT));
        assertThat(badge.getShape(), is(ShapeType.rounded));
        assertThat(badge.getColorFieldId(), is("bcfi1"));
        assertThat(badge.getImageFieldId(), is("bifi1"));
        assertThat(badge.getImagePosition(), is(Position.RIGHT));
        assertThat(badge.getImageShape(), is(ShapeType.square));
        assertThat(badge.getText(), nullValue());
        assertThat(badge.getColor(), nullValue());
        assertThat(badge.getImage(), nullValue());

        badge = ((Tree) page.getRegions().get("single").get(0).getContent().get(1)).getBadge();
        assertThat(badge.getFieldId(), nullValue());
        assertThat(badge.getPosition(), is(Position.RIGHT));
        assertThat(badge.getShape(), is(ShapeType.square));
        assertThat(badge.getColorFieldId(), nullValue());
        assertThat(badge.getImageFieldId(), is("bifi2"));
        assertThat(badge.getImagePosition(), is(Position.LEFT));
        assertThat(badge.getImageShape(), is(ShapeType.circle));
        assertThat(badge.getText(), nullValue());
        assertThat(badge.getColor(), nullValue());
        assertThat(badge.getImage(), nullValue());
    }

    @Test
    public void testButtons() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/badge/testButtons.page.xml")
                .get(new PageContext("testButtons"));

        Badge badge = ((ButtonField) ((FormWidgetComponent) page.getWidget().getComponent())
                .getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0)).getBadge();
        assertThat(badge.getText(), is("`bfi1`"));
        assertThat(badge.getPosition(), is(Position.LEFT));
        assertThat(badge.getShape(), is(ShapeType.rounded));
        assertThat(badge.getColor(), is("danger"));
        assertThat(badge.getImage(), is("/static/candidate.png"));
        assertThat(badge.getImagePosition(), is(Position.RIGHT));
        assertThat(badge.getImageShape(), is(ShapeType.square));
        assertThat(badge.getFieldId(), nullValue());
        assertThat(badge.getColorFieldId(), nullValue());
        assertThat(badge.getImageFieldId(), nullValue());

        badge = page.getWidget().getToolbar().getButton("b2").getBadge();
        assertThat(badge.getText(), nullValue());
        assertThat(badge.getPosition(), is(Position.RIGHT));
        assertThat(badge.getShape(), is(ShapeType.circle));
        assertThat(badge.getColor(), nullValue());
        assertThat(badge.getImage(), is("`img`"));
        assertThat(badge.getImagePosition(), is(Position.LEFT));
        assertThat(badge.getImageShape(), is(ShapeType.circle));
        assertThat(badge.getFieldId(), nullValue());
        assertThat(badge.getColorFieldId(), nullValue());
        assertThat(badge.getImageFieldId(), nullValue());

        badge = page.getWidget().getToolbar().getButton("b3").getBadge();
        assertThat(badge, nullValue());
    }

    @Test
    public void testMenuItem() {
        Application application = compile("net/n2oapp/framework/config/metadata/compile/badge/testMenuItem.application.xml")
                .get(new ApplicationContext("testMenuItem"));

        Badge badge = application.getHeader().getMenu().getItems().get(0).getBadge();
        assertThat(badge.getText(), is("`badge`"));
        assertThat(badge.getPosition(), is(Position.LEFT));
        assertThat(badge.getShape(), is(ShapeType.rounded));
        assertThat(badge.getColor(), is("warning"));
        assertThat(badge.getImage(), is("`img`"));
        assertThat(badge.getImagePosition(), is(Position.RIGHT));
        assertThat(badge.getImageShape(), is(ShapeType.square));
        assertThat(badge.getFieldId(), nullValue());
        assertThat(badge.getColorFieldId(), nullValue());
        assertThat(badge.getImageFieldId(), nullValue());

        badge = application.getHeader().getMenu().getItems().get(1).getBadge();
        assertThat(badge.getText(), nullValue());
        assertThat(badge.getPosition(), is(Position.RIGHT));
        assertThat(badge.getShape(), is(ShapeType.circle));
        assertThat(badge.getColor(), nullValue());
        assertThat(badge.getImage(), is("/static/candidate.png"));
        assertThat(badge.getImagePosition(), is(Position.LEFT));
        assertThat(badge.getImageShape(), is(ShapeType.circle));
        assertThat(badge.getFieldId(), nullValue());
        assertThat(badge.getColorFieldId(), nullValue());
        assertThat(badge.getImageFieldId(), nullValue());

        badge = application.getHeader().getMenu().getItems().get(2).getBadge();
        assertThat(badge, nullValue());
    }

}
