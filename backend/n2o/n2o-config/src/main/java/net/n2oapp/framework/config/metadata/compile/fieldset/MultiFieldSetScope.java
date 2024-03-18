package net.n2oapp.framework.config.metadata.compile.fieldset;

/**
 * Информация о multi-set, в который вложено поле или филдсет
 */
public class MultiFieldSetScope {
    /**
     * Идентификатор multi-set
     */
    private String id;
    /**
     * Переменая для итерирования внутри multi-set, сейчас для первого уровня index, для всех последующих $index_N
     */
    private String index;
    /**
     * Индекс вложенности мультисета
     */
    private int idx = 0;
    /**
     * Путь до родительского multi-set с индексами
     */
    private String parentPathWithIndexes;

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

    public String getId() {
        return id;
    }

    public String getIndex() {
        return index;
    }

    public String getPathWithIndexes() {
        if (parentPathWithIndexes == null || parentPathWithIndexes.isEmpty())
            return id + "[index]";
        return parentPathWithIndexes + "." + id + "[" + index + "]";
    }
}
