package net.n2oapp.framework.api.event;

import net.n2oapp.framework.api.metadata.SourceMetadata;

/**
 * Абстрактный слушатель изменений метаданных
 */
public abstract class MetadataChangeListener implements N2oEventListener<MetadataChangedEvent> {
    private boolean any = false;
    private Class<? extends SourceMetadata> sourceClass;
    private String metadataId;

    /**
     * Конструктор слушателя изменений конкретной метаданной
     *
     * @param metadataId  Идентификатор метаданной
     * @param sourceClass Класс метаданной
     */
    public MetadataChangeListener(String metadataId, Class<? extends SourceMetadata> sourceClass) {
        this.metadataId = metadataId;
        this.sourceClass = sourceClass;
    }

    /**
     * Конструктор слушателя изменений любых метаданных
     */
    public MetadataChangeListener() {
        this.any = true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isApply(MetadataChangedEvent event) {
        return any || event.isAll()
                || (sourceClass.isAssignableFrom(event.getBaseSourceClass())
                    && metadataId.equals(event.getSourceId()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleEvent(MetadataChangedEvent event) {
        if (event.isAll())
            handleAllMetadataChange();
        else
            handleMetadataChange(event.getSourceId(), event.getBaseSourceClass());
    }

    public abstract void handleAllMetadataChange();

    public abstract void handleMetadataChange(String id, Class<? extends SourceMetadata> sourceClass);
}
