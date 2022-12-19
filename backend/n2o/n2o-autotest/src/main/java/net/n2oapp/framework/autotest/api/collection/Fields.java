package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.Markdown;
import net.n2oapp.framework.autotest.api.component.field.Field;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.snippet.Snippet;

/**
 * Поля формы для автотестирования
 */
public interface Fields extends ComponentsCollection {
    StandardField field(String label);

    StandardField field(Condition findBy);

    Markdown markdown();

    <T extends Snippet> T field(Class<T> componentClass);

    <T extends Field> T field(String label, Class<T> componentClass);

    <T extends Field> T field(Condition findBy, Class<T> componentClass);

    <T extends Snippet> T field(int index, Class<T> componentClass);
}
