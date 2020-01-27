package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.field.Field;
import net.n2oapp.framework.autotest.api.component.field.StandardField;

/**
 * Поля формы для автотестирования
 */
public interface Fields {
    StandardField field(String label);

    StandardField field(Condition findBy);

    <T extends Field> T field(String label, Class<T> componentClass);

    <T extends Field> T field(Condition findBy, Class<T> componentClass);
}
