package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;


@Getter
@Setter
public class N2oTable extends N2oAbstractTable {
    private FilterPosition filterPosition;
    private Boolean filterOpened;
    private SourceComponent[] filters;
    private String filtersDefaultValuesQueryId;
    private Boolean searchOnChange;
    private ChildrenToggle children;


    public enum FilterPosition {
        top, left;

        public String getName() {
            return this.name();
        }
    }

    /**
     * Виды отображения дочерних записей таблицы
     */
    public enum ChildrenToggle {
        collapse,   // свернутый
        expand      // раскрытый
    }
}
