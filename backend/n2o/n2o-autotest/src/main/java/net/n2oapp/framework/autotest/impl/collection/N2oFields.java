package net.n2oapp.framework.autotest.impl.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.Markdown;
import net.n2oapp.framework.autotest.api.component.field.Field;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.snippet.Snippet;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

public class N2oFields extends N2oComponentsCollection implements Fields {
    @Override
    public StandardField field(String label) {
        return field(label, StandardField.class);
    }

    @Override
    public StandardField field(Condition findBy) {
        return field(findBy, StandardField.class);
    }

    @Override
    public <T extends Snippet> T field(Class<T> componentClass) {
        return field(0, componentClass);
    }

    @Override
    public <T extends Field> T field(String label, Class<T> componentClass) {
        return component(elements().findBy(Condition.text(label)), componentClass);
    }

    @Override
    public <T extends Field> T field(Condition findBy, Class<T> componentClass) {
        return component(elements().findBy(findBy), componentClass);
    }

    @Override
    public <T extends Snippet> T field(int index, Class<T> componentClass) {
        return component(elements().get(index), componentClass);
    }
}
