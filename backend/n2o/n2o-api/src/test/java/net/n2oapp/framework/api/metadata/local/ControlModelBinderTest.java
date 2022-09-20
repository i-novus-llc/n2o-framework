package net.n2oapp.framework.api.metadata.local;

import net.n2oapp.context.CacheTemplateByMapMock;
import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.interval.N2oDateInterval;
import net.n2oapp.framework.api.metadata.control.list.N2oClassifier;
import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.metadata.local.view.widget.control.ControlModelBinder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * User: operhod
 * Date: 06.06.14
 * Time: 15:38
 */
public class ControlModelBinderTest {

    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        StaticSpringContext staticSpringContext = new StaticSpringContext();
        staticSpringContext.setApplicationContext(applicationContext);
        staticSpringContext.setCacheTemplate(new CacheTemplateByMapMock());
    }

    @Test
    public void test() throws Exception {
        List<N2oField> controls = new ArrayList<>();
        controls.add(new N2oInputText("id"));
        controls.add(new N2oClassifier("gender"));
        controls.add(new N2oInputText("gender.id"));
        controls.add(new N2oClassifier("nat.group"));
        controls.add(new N2oClassifier("nat.group.subgroup"));
        controls.add(new N2oDateInterval("bdate"));

        ControlModelBinder picker = new ControlModelBinder(controls);

        //простые поля
        List<N2oField> list = picker.bind("id");
        assert list.size() == 1;
        assert list.get(0).getId().equals("id");

        //сложные поля
        list = picker.bind("gender.id");
        assert list.size() == 2;
        assert list.get(0).getId().equals("gender.id");
        assert list.get(1).getId().equals("gender");

        list = picker.bind("nat.group.subgroup.name");
        assert list.size() == 2;
        assert list.get(0).getId().equals("nat.group");
        assert list.get(1).getId().equals("nat.group.subgroup");

        list = picker.bind("bdate.begin");
        assert list.size() == 1;
        assert list.get(0).getId().equals("bdate");

        list = picker.bind("genders*.id");
        assert list.size() == 1;
        assert list.get(0).getId().equals("genders");
    }
}
