package net.n2oapp.framework.api.metadata.global.view.widget.table;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;

@Getter
@Setter
public abstract class N2oAbstractTable extends N2oAbstractListWidget {
    @N2oAttribute("Список колонок")
    private AbstractColumn[] columns;
    @N2oAttribute("Автоматическое выделение первой строки")
    private Boolean autoSelect;
    @N2oAttribute("Вариант выбора строки")
    private RowSelectionEnum selection;
    @N2oAttribute("Размер")
    private Size tableSize;
    @N2oAttribute("Максимальная ширина")
    private String width;
    @N2oAttribute("Максимальная высота")
    private String height;
    @N2oAttribute("Перенос текста в ячейке")
    private Boolean textWrap;
    @N2oAttribute("Наличие чекбоксов в первом столбце")
    private Boolean checkboxes;
    @N2oAttribute("Срабатывание чекбокса при клике по строке")
    private Boolean checkOnSelect;
}
