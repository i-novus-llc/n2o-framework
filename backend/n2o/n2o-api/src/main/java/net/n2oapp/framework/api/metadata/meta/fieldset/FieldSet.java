package net.n2oapp.framework.api.metadata.meta.fieldset;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;
import net.n2oapp.framework.api.metadata.global.view.fieldset.FieldLabelAlign;
import net.n2oapp.framework.api.metadata.global.view.fieldset.FieldLabelLocation;
import net.n2oapp.framework.api.metadata.meta.badge.Badge;
import net.n2oapp.framework.api.metadata.meta.control.ControlDependency;
import net.n2oapp.framework.api.metadata.meta.control.Field;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель FieldSetа
 */
@Getter
@Setter
public abstract class FieldSet extends Component implements Compiled {
    @JsonProperty
    private String label;
    @JsonProperty
    private String description;
    @JsonProperty
    private String help;
    @JsonProperty
    private LabelPosition labelPosition;
    @JsonProperty
    private Object labelWidth;
    @JsonProperty
    private LabelAlignment labelAlignment;
    @JsonProperty
    private List<Row> rows;
    @JsonProperty
    private Object visible;
    @JsonProperty
    private Object enabled;
    @JsonProperty
    private ControlDependency[] dependency;
    @JsonProperty
    private Badge badge;

    @Getter
    @Setter
    public static class Row implements Compiled, JsonPropertiesAware {
        @JsonProperty
        private String className;
        @JsonProperty
        private Map<String, String> style;
        @JsonProperty
        private List<Column> cols;
        private Map<String, Object> properties;
    }

    @Getter
    @Setter
    public static class Column implements Compiled, JsonPropertiesAware {
        @JsonProperty
        private String className;
        @JsonProperty
        private Map<String, String> style;
        @JsonProperty
        private Integer size;
        @JsonProperty
        private Object visible;
        @JsonProperty
        private List<FieldSet> fieldsets;
        @JsonProperty
        private List<Field> fields;
        private Map<String, Object> properties;
    }

    public enum LabelPosition {
        LEFT("left", FieldLabelLocation.LEFT, null),
        RIGHT("right", FieldLabelLocation.RIGHT, null),
        TOP_LEFT("top-left", FieldLabelLocation.TOP, FieldLabelAlign.LEFT),
        TOP_RIGHT("top-right", FieldLabelLocation.TOP, FieldLabelAlign.RIGHT);

        private String id;
        private FieldLabelLocation mapLocation;
        private FieldLabelAlign mapAlign;

        LabelPosition(String id, FieldLabelLocation mapLocation,
                      FieldLabelAlign mapAlign) {
            this.id = id;
            this.mapLocation = mapLocation;
            this.mapAlign = mapAlign;
        }

        @JsonValue
        public String getId() {
            return id;
        }

        public static LabelPosition map(FieldLabelLocation location,
                                        FieldLabelAlign align) {
            for (LabelPosition position : values()) {
                if (position.mapLocation.equals(location)
                        && (position.mapAlign == null || align == null || position.mapAlign.equals(align)))
                    return position;
            }
            return null;
        }
    }

    public enum LabelAlignment {
        LEFT("left", FieldLabelAlign.LEFT),
        RIGHT("right", FieldLabelAlign.RIGHT);

        private String id;
        private FieldLabelAlign map;

        LabelAlignment(String id, FieldLabelAlign map) {
            this.id = id;
            this.map = map;
        }

        @JsonValue
        public String getId() {
            return id;
        }

        public FieldLabelAlign getMap() {
            return map;
        }

        public static LabelAlignment map(FieldLabelAlign map) {
            for (LabelAlignment alignment : values()) {
                if (alignment.getMap().equals(map))
                    return alignment;
            }
            return null;
        }
    }

}
