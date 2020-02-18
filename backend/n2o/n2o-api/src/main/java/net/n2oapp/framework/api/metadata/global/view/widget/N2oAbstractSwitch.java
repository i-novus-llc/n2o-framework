package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

/**
 * Абстрактная модель переключателя элементов
 */
@Getter
@Setter
public abstract class N2oAbstractSwitch<T> implements NamespaceUriAware {
    private String namespaceUri;
    private String valueFieldId;
    private Case<T>[] cases;
    private T defaultCase;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Case<T> implements Source {
        private String value;
        private T item;
    }
}
