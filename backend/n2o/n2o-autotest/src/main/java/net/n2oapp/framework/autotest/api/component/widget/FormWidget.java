package net.n2oapp.framework.autotest.api.component.widget;

import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.collection.Fields;

/**
 * Виджет - форма для автотестирования
 */
public interface FormWidget extends StandardWidget {

    /**
     * @return Поля формы для автотестирования
     */
    Fields fields();

    /**
     * @return Филдсеты для автотестирования
     */
    FieldSets fieldsets();
}
