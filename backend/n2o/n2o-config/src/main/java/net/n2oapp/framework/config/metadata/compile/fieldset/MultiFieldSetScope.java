package net.n2oapp.framework.config.metadata.compile.fieldset;

import lombok.Getter;

/**
 * Информация о multi-set, в который вложено поле или филдсет
 */
public class MultiFieldSetScope {
    /**
     * Идентификатор multi-set
     */
    @Getter
    private final String id;
    /**
     * Переменная для итерирования внутри multi-set, сейчас для первого уровня index, для всех последующих $index_N
     */
    @Getter
    private final String index;
    /**
     * Индекс вложенности мультисета
     */
    private int idx = 0;
    /**
     * Путь до родительского multi-set с индексами
     */
    private final String parentPathWithIndexes;

    public MultiFieldSetScope(String id, MultiFieldSetScope parent) {
        this.id = id;
        if (parent == null) {
            index = "index";
            parentPathWithIndexes = "";
        } else {
            idx = parent.idx + 1;
            index = "$index_" + idx;
            parentPathWithIndexes = parent.getPathWithIndexes();
        }
    }

    public String getPathWithIndexes() {
        if (parentPathWithIndexes == null || parentPathWithIndexes.isEmpty())
            return id + "[index]";
        return parentPathWithIndexes + "." + id + "[" + index + "]";
    }
}
