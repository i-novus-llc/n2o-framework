package net.n2oapp.framework.api.metadata.control.multi;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;

/**
 * Компонент ввода checkbox-grid
 */
@Getter
@Setter
public class N2oCheckboxGrid extends N2oMultiListFieldAbstract {

    private Column[] columns;
    private transient List<Column> compiledColumns;

    @Getter
    @Setter
    public static class Column implements Serializable {
        private String name;
        private String columnFieldId;

        public Column() {
        }

        public Column(Column other) {
            this.name = other.name;
            this.columnFieldId = other.columnFieldId;
        }

        public Column(String name, String columnFieldId) {
            this.name = name;
            this.columnFieldId = columnFieldId;
        }
    }
}
