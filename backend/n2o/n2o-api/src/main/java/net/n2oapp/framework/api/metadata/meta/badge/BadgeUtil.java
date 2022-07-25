package net.n2oapp.framework.api.metadata.meta.badge;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Утилитный класс для работы со значками
 */
public class BadgeUtil {

    public static final String POSITION = ".badge.position";
    public static final String SHAPE = ".badge.shape";
    public static final String IMAGE_POSITION = ".badge.image_position";
    public static final String IMAGE_SHAPE = ".badge.image_shape";

    private BadgeUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Компиляция значка по модели BadgePresence
     *
     * @param source         Модель реализующая BadgePresence
     * @param propertyPrefix Префикс свойств значений по умолчанию
     * @param p              Процессор сборки метаданных
     * @return Клиентская модель значка
     */
    public static Badge compileBadge(BadgePresence source, String propertyPrefix, CompileProcessor p) {
        if (source.getBadgeFieldId() == null && source.getBadgeColorFieldId() == null && source.getBadgeImageFieldId() == null)
            return null;
        return Badge.builder()
                .fieldId(source.getBadgeFieldId())
                .colorFieldId(source.getBadgeColorFieldId())
                .imageFieldId(source.getBadgeImageFieldId())
                .imagePosition(p.cast(source.getBadgeImagePosition(), p.resolve(property(propertyPrefix + IMAGE_POSITION), Position.class)))
                .imageShape(p.cast(source.getBadgeImageShape(), p.resolve(property(propertyPrefix + IMAGE_SHAPE), ShapeType.class)))
                .position(p.cast(source.getBadgePosition(), p.resolve(property(propertyPrefix + POSITION), Position.class)))
                .shape(p.cast(source.getBadgeShape(), p.resolve(property(propertyPrefix + SHAPE), ShapeType.class)))
                .build();
    }
}
