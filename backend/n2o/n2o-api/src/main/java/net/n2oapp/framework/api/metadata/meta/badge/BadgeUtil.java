package net.n2oapp.framework.api.metadata.meta.badge;

import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

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
     * Компиляция значка, не использующего ссылки на поля
     *
     * @param source         Модель реализующая BadgePresence
     * @param propertyPrefix Префикс свойств значений по умолчанию
     * @param p              Процессор сборки метаданных
     * @return Клиентская модель значка
     */
    public static Badge compileSimpleBadge(BadgeAware source, String propertyPrefix, CompileProcessor p) {
        if (source.getBadge() == null && source.getBadgeColor() == null && source.getBadgeImage() == null)
            return null;
        Badge badge = new Badge();
        badge.setText(p.resolveJS(source.getBadge()));
        badge.setImage(p.resolveJS(source.getBadgeImage()));
        badge.setColor(p.resolveJS(source.getBadgeColor()));
        compileDefaults(badge, source, propertyPrefix, p);
        return badge;
    }

    /**
     * Связывание данных значка, не использующего ссылки на поля
     *
     * @param badge Значок
     * @param p Процессор связывания метаданных с данными
     */
    public static void bindSimpleBadge(Badge badge, BindProcessor p) {
        if (badge != null) {
            badge.setText(p.resolve(badge.getText(), String.class));
            badge.setColor(p.resolve(badge.getColor(), String.class));
            badge.setImage(p.resolve(badge.getImage(), String.class));
        }
    }

    /**
     * Компиляция значка, ссылающегося на поля
     *
     * @param source         Модель реализующая BadgePresence
     * @param propertyPrefix Префикс свойств значений по умолчанию
     * @param p              Процессор сборки метаданных
     * @return Клиентская модель значка
     */
    public static Badge compileReferringBadge(BadgeAware source, String propertyPrefix, CompileProcessor p) {
        if (source.getBadgeFieldId() == null && source.getBadgeColorFieldId() == null && source.getBadgeImageFieldId() == null)
            return null;
        Badge badge = new Badge();
        badge.setFieldId(source.getBadgeFieldId());
        badge.setColorFieldId(source.getBadgeColorFieldId());
        badge.setImageFieldId(source.getBadgeImageFieldId());
        compileDefaults(badge, source, propertyPrefix, p);
        return badge;
    }

    /**
     * Компиляция дефолтных свойств значка
     *
     * @param compiled       Клиентская модель значка
     * @param source         Модель реализующая BadgePresence
     * @param propertyPrefix Префикс свойств значений по умолчанию
     * @param p              Процессор сборки метаданных
     */
    private static void compileDefaults(Badge compiled, BadgeAware source, String propertyPrefix, CompileProcessor p) {
        compiled.setShape(castDefault(source.getBadgeShape(),
                () -> p.resolve(property(propertyPrefix + SHAPE), ShapeType.class)));
        compiled.setPosition(castDefault(source.getBadgePosition(),
                () -> p.resolve(property(propertyPrefix + POSITION), Position.class)));
        compiled.setImagePosition(castDefault(source.getBadgeImagePosition(),
                () -> p.resolve(property(propertyPrefix + IMAGE_POSITION), Position.class)));
        compiled.setImageShape(castDefault(source.getBadgeImageShape(),
                () -> p.resolve(property(propertyPrefix + IMAGE_SHAPE), ShapeType.class)));
    }
}
