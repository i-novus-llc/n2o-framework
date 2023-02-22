package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.widget.table.TableSimpleHeader;
import net.n2oapp.framework.autotest.api.component.widget.table.TableHeader;

/**
 * Заголовки столбцов таблицы для автотестирования
 */
public interface TableHeaders extends ComponentsCollection {

    /**
     * <p>
     *     Возвращает простой заголовок столбца таблицы по номеру
     * </p>
     *
     * <p>For example: {@code
     *     headers().header(0)
     * }</p>
     *
     * @param index номер заголовока в таблице
     * @return Компонент заголовок столбца таблицы для автотестирования
     */
    TableSimpleHeader header(int index);

    /**
     * <p>
     *     Возвращает простой заголовок столбца таблицы по условию
     * </p>
     *
     * <p>For example: {@code
     *     headers().header(Condition.text("Фамилия"))
     * }</p>
     *
     * @param findBy условие поиска
     * @return Компонент простой заголовок столбца таблицы для автотестирования
     */
    TableSimpleHeader header(Condition findBy);

    /**
     * <p>
     *     Возвращает заголовок столбца таблицы типа, наследуемого от TableHeader, по порядковому номеру
     * </p>
     *
     * <p>For example: {@code
     *     headers().header(0, TableMultiHeader.class)
     * }</p>
     *
     * @param index номер заголовока в таблице
     * @param componentClass возвращаемый тип заголовока
     * @return Компонент заголовок столбца таблицы для автотестирования
     */
    <T extends TableHeader> T header(int index, Class<T> componentClass);

    /**
     * <p>
     *     Возвращает заголовок столбца таблицы типа, наследуемого от TableHeader, по условию
     * </p>
     *
     * <p>For example: {@code
     *     headers().header(Condition.text("Фамилия"), TableMultiHeader.class)
     * }</p>
     *
     * @param findBy условие поиска
     * @param componentClass возвращаемый тип заголовока
     * @return Компонент заголовок столбца таблицы для автотестирования
     */
    <T extends TableHeader> T header(Condition findBy, Class<T> componentClass);
}
