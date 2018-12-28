package net.n2oapp.framework.api.event;

import net.n2oapp.framework.api.metadata.SourceMetadata;

/**
 * Событие, когда метаданная изменилась
 */
public class MetadataChangedEvent extends N2oEvent {
    private boolean all = false;
    private String sourceId;
    private Class<? extends SourceMetadata> baseSourceClass;

    /**
     * Конструктор события изменения всех метаданных
     */
    public MetadataChangedEvent(Object source) {
        super(source);
        this.all = true;
    }

    /**
     * Конструктор события изменения одной метаданной
     */
    public MetadataChangedEvent(Object source, String sourceId, Class<? extends SourceMetadata> baseSourceClass) {
        super(source);
        this.sourceId = sourceId;
        this.baseSourceClass = baseSourceClass;
    }

    public String getSourceId() {
        return sourceId;
    }

    public Class<? extends SourceMetadata> getBaseSourceClass() {
        return baseSourceClass;
    }

    public boolean isAll() {
        return all;
    }
}
