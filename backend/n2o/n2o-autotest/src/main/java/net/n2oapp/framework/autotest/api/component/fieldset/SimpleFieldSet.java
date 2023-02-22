package net.n2oapp.framework.autotest.api.component.fieldset;

import net.n2oapp.framework.autotest.api.collection.Fields;

/**
 * Простой филдсет для автотестирования
 */
public interface SimpleFieldSet extends FieldSet {
    /**
     * Возвращает все поля внутри филдсета
     * @return поля формы для автотестирования
     */
    Fields fields();
}
