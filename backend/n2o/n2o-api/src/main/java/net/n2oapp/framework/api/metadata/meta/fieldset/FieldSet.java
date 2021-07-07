package net.n2oapp.framework.api.metadata.meta.fieldset;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
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

    @Getter
    @Setter
    public static class Row implements Compiled {
        @JsonProperty
        private String className;
        @JsonProperty
        private Map<String, String> style;
        @JsonProperty
        private List<Column> cols;
    }

    @Getter
    @Setter
    public static class Column implements Compiled {
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
    }

    public enum LabelPosition {
        LEFT("left", N2oFieldSet.FieldLabelLocation.left, null),
        RIGHT("right", N2oFieldSet.FieldLabelLocation.right, null),
        TOP_LEFT("top-left", N2oFieldSet.FieldLabelLocation.top, N2oFieldSet.FieldLabelAlign.left),
        TOP_RIGHT("top-right", N2oFieldSet.FieldLabelLocation.top, N2oFieldSet.FieldLabelAlign.right);

        private String id;
        private N2oFieldSet.FieldLabelLocation mapLocation;
        private N2oFieldSet.FieldLabelAlign mapAlign;

        LabelPosition(String id, N2oFieldSet.FieldLabelLocation mapLocation,
                      N2oFieldSet.FieldLabelAlign mapAlign) {
            this.id = id;
            this.mapLocation = mapLocation;
            this.mapAlign = mapAlign;
        }

        @JsonValue
        public String getId() {
            return id;
        }

        public static LabelPosition map(N2oFieldSet.FieldLabelLocation location,
                                        N2oFieldSet.FieldLabelAlign align) {
            for (LabelPosition position : values()) {
                if (position.mapLocation.equals(location)
                        && (position.mapAlign == null || align == null || position.mapAlign.equals(align)))
                    return position;
            }
            return null;
        }
    }

    public enum LabelAlignment {
        LEFT("left", N2oFieldSet.FieldLabelAlign.left),
        RIGHT("right", N2oFieldSet.FieldLabelAlign.right);

        private String id;
        private N2oFieldSet.FieldLabelAlign map;

        LabelAlignment(String id, N2oFieldSet.FieldLabelAlign map) {
            this.id = id;
            this.map = map;
        }

        @JsonValue
        public String getId() {
            return id;
        }

        public N2oFieldSet.FieldLabelAlign getMap() {
            return map;
        }

        public static LabelAlignment map(N2oFieldSet.FieldLabelAlign map) {
            for (LabelAlignment alignment : values()) {
                if (alignment.getMap().equals(map))
                    return alignment;
            }
            return null;
        }
    }

}
