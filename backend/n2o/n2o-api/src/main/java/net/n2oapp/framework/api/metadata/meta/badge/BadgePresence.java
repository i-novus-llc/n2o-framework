package net.n2oapp.framework.api.metadata.meta.badge;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;

/**
 * Наличие значка у компонента
 */
public interface BadgePresence {

    static Badge compileBadge(BadgePresence source, Position defaultPosition, ShapeType defaultShape,
                              Position defaultImagePosition, ShapeType defaultImageShape, CompileProcessor p) {
        if (source.getBadgeFieldId() == null && source.getBadgeColorFieldId() == null && source.getBadgeImageFieldId() == null)
            return null;
        return Badge.builder()
                .fieldId(source.getBadgeFieldId())
                .colorFieldId(source.getBadgeColorFieldId())
                .imageFieldId(source.getBadgeImageFieldId())
                .imagePosition(p.cast(source.getBadgeImagePosition(), defaultImagePosition))
                .imageShape(p.cast(source.getBadgeImageShape(), defaultImageShape))
                .position(p.cast(source.getBadgePosition(), defaultPosition))
                .shape(p.cast(source.getBadgeShape(), defaultShape))
                .build();
    }

    Position getBadgePosition();

    ShapeType getBadgeShape();

    Position getBadgeImagePosition();

    ShapeType getBadgeImageShape();

    String getBadgeImageFieldId();

    String getBadgeFieldId();

    String getBadgeColorFieldId();
}
