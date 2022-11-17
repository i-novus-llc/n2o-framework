package net.n2oapp.framework.config.metadata.compile.fieldset;

/**
 * Информация о multi-set, в который вложено поле или филдсет
 */
public class MultiFieldSetScope {
    private String id;

    public MultiFieldSetScope(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
