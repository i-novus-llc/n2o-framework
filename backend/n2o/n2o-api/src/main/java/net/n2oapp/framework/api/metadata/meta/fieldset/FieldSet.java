package net.n2oapp.framework.api.metadata.meta.fieldset;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;
import net.n2oapp.framework.api.metadata.global.view.fieldset.FieldLabelAlignEnum;
import net.n2oapp.framework.api.metadata.global.view.fieldset.FieldLabelLocationEnum;
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
    private LabelPositionEnum labelPosition;
    @JsonProperty
    private Object labelWidth;
    @JsonProperty
    private LabelAlignmentEnum labelAlignment;
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

    public enum LabelPositionEnum {
        LEFT("left", FieldLabelLocationEnum.LEFT, null),
        RIGHT("right", FieldLabelLocationEnum.RIGHT, null),
        TOP_LEFT("top-left", FieldLabelLocationEnum.TOP, FieldLabelAlignEnum.LEFT),
        TOP_RIGHT("top-right", FieldLabelLocationEnum.TOP, FieldLabelAlignEnum.RIGHT);

        private String id;
        private FieldLabelLocationEnum mapLocation;
        private FieldLabelAlignEnum mapAlign;

        LabelPositionEnum(String id, FieldLabelLocationEnum mapLocation,
                          FieldLabelAlignEnum mapAlign) {
            this.id = id;
            this.mapLocation = mapLocation;
            this.mapAlign = mapAlign;
        }

        @JsonValue
        public String getId() {
            return id;
        }

        public static LabelPositionEnum map(FieldLabelLocationEnum location,
                                            FieldLabelAlignEnum align) {
            for (LabelPositionEnum position : values()) {
                if (position.mapLocation.equals(location)
                        && (position.mapAlign == null || align == null || position.mapAlign.equals(align)))
                    return position;
            }
            return null;
        }
    }

    public enum LabelAlignmentEnum {
        LEFT("left", FieldLabelAlignEnum.LEFT),
        RIGHT("right", FieldLabelAlignEnum.RIGHT);

        private String id;
        private FieldLabelAlignEnum map;

        LabelAlignmentEnum(String id, FieldLabelAlignEnum map) {
            this.id = id;
            this.map = map;
        }

        @JsonValue
        public String getId() {
            return id;
        }

        public FieldLabelAlignEnum getMap() {
            return map;
        }

        public static LabelAlignmentEnum map(FieldLabelAlignEnum map) {
            for (LabelAlignmentEnum alignment : values()) {
                if (alignment.getMap().equals(map))
                    return alignment;
            }
            return null;
        }
    }

}
