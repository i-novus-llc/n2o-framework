import React, { Component, Fragment } from 'react'
import { pure } from 'recompose'
import isArray from 'lodash/isArray'
import isString from 'lodash/isString'
import get from 'lodash/get'
import cn from 'classnames'
import PropTypes from 'prop-types'
import { Resizable } from 'react-resizable'

import Icon from '../../snippets/Icon/Icon'

import AdvancedTableFilter from './AdvancedTableFilter'

/**
 * Компонент ячейки заголовка
 * @param children - компонент потомок
 * @param className - класс ячейки
 * @param columnId - id колонки
 * @param dataIndex - id ключа с данными из модели
 * @param id - id колонки
 * @param index - index колонки
 * @param label - текст заголовка
 * @param multiHeader - флаг многоуровневого заголовка
 * @param onFilter - callback на фильтрацию
 * @param onResize - callback на изменение размера колонок
 * @param onSort - функция вызова сортировки
 * @param sorting - настройки сортировки
 * @param title - компонент в ячейки заголовка
 *  @param width - длина колонки
 *  @param resizable - флаг функции resize колонки
 *  @param selectionHead - флаг чекбокса в заголовке
 *  @param selectionClass - класс для чекбокса в заголовке
 */
class AdvancedTableHeaderCell extends Component {
    constructor(props) {
        super(props)

        this.renderCell = this.renderCell.bind(this)
        this.renderMultiCell = this.renderMultiCell.bind(this)
        this.renderStringChild = this.renderStringChild.bind(this)
    }

    renderMultiCell() {
        const { colSpan, rowSpan, className, id, label, sorting } = this.props

        return (
            <th
                title={label}
                className={cn(
                    'n2o-advanced-table-header-cel',
                    'n2o-advanced-table-header-text-center',
                    className,
                )}
                colSpan={colSpan}
                rowSpan={rowSpan}
            >
                {React.createElement(this.props.component, {
                    ...this.props,
                    sorting: sorting && sorting[id],
                })}
            </th>
        )
    }

    renderStringChild() {
        const { className, children, colSpan, rowSpan } = this.props

        return (
            <th
                className={cn(
                    'n2o-advanced-table-header-cel',
                    'n2o-advanced-table-header-text-center',
                    className,
                )}
                colSpan={colSpan}
                rowSpan={rowSpan}
            >
                {children}
            </th>
        )
    }

    renderCell() {
        const {
            id,
            multiHeader,
            children,
            selectionHead,
            selectionClass,
            filterable,
            colSpan,
            rowSpan,
            icon,
            onFilter,
            filters,
            label,
            title,
            filterControl,
            as,
            style,
            className,
        } = this.props

        let cellContent = null

        if (isString(title)) {
            cellContent = title
        } else if (isString(children)) {
            return this.renderStringChild()
        } else if (multiHeader && isArray(children)) {
            return this.renderMultiCell()
        } else {
            cellContent = children
        }

        const ElementType = as || 'th'

        return (
            <ElementType
                title={label}
                rowSpan={rowSpan}
                colSpan={colSpan}
                style={style}
                className={cn('n2o-advanced-table-header-cel', {
                    [selectionClass]: selectionHead,
                    'n2o-advanced-table-header-text-center': multiHeader,
                    'd-none': !get(children, 'props.needRender', true),
                })}
            >
                <div
                    className={cn('n2o-advanced-table-header-cell-content', className)}
                >
                    {icon && <Icon name={icon} />}
                    {filterable ? (
                        <AdvancedTableFilter
                            id={id}
                            onFilter={onFilter}
                            value={filters && filters[id]}
                            control={filterControl}
                        >
                            {cellContent}
                        </AdvancedTableFilter>
                    ) : (
                        cellContent
                    )}
                </div>
            </ElementType>
        )
    }

    render() {
        const { width, onResize, resizable } = this.props

        return (
            <>
                {resizable && width ? (
                    <Resizable
                        width={width}
                        height={0}
                        onResize={onResize}
                        handleSize={[10, 10]}
                    >
                        {this.renderCell()}
                    </Resizable>
                ) : (
                    this.renderCell()
                )}
            </>
        )
    }
}

AdvancedTableHeaderCell.propTypes = {
    children: PropTypes.oneOfType([
        PropTypes.array,
        PropTypes.string,
        PropTypes.object,
    ]),
    className: PropTypes.string,
    columnId: PropTypes.string,
    dataIndex: PropTypes.string,
    id: PropTypes.string,
    index: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    label: PropTypes.string,
    multiHeader: PropTypes.bool,
    onCell: PropTypes.func,
    onFilter: PropTypes.func,
    onHeaderCell: PropTypes.func,
    onResize: PropTypes.func,
    onSort: PropTypes.func,
    sorting: PropTypes.object,
    title: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.func,
        PropTypes.node,
    ]),
    width: PropTypes.number,
    resizable: PropTypes.bool,
    selectionHead: PropTypes.bool,
    selectionClass: PropTypes.string,
    filterControl: PropTypes.object,
}

AdvancedTableHeaderCell.defaultProps = {
    filterControl: {},
    multiHeader: false,
    resizable: false,
    selectionHead: false,
    sorting: {},
    onResize: () => {},
    onSort: () => {},
    onFilter: () => {},
}

export default pure(AdvancedTableHeaderCell)
