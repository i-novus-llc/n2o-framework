import React, { Component } from 'react'
import { pure } from 'recompose'
import isArray from 'lodash/isArray'
import isString from 'lodash/isString'
import get from 'lodash/get'
import classNames from 'classnames'
import PropTypes from 'prop-types'
import { Resizable } from 'react-resizable'

import { Icon } from '../../snippets/Icon/Icon'
// eslint-disable-next-line import/no-named-as-default
import TextTableHeader from '../Table/headers/TextTableHeader'

// eslint-disable-next-line import/no-named-as-default
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
        const {
            colSpan,
            rowSpan,
            className,
            id,
            label,
            sorting,
            component,
            alignment,
            children = [],
            visible = true,
        } = this.props

        if (!visible) {
            return null
        }

        const calculatedColSpan = children.filter(({ visible }) => visible).length

        return (
            <th
                title={label}
                className={classNames(
                    'n2o-advanced-table-header-cel',
                    className,
                    {
                        'n2o-advanced-table-header-text-center': !alignment,
                        [`alignment-${alignment}`]: alignment,
                    },
                )}
                colSpan={calculatedColSpan || colSpan}
                rowSpan={rowSpan}
            >
                {React.createElement(component, {
                    ...this.props,
                    sorting: sorting && sorting[id],
                })}
            </th>
        )
    }

    renderStringChild() {
        const { className, children, colSpan, rowSpan, alignment } = this.props

        return (
            <th
                className={classNames(
                    'n2o-advanced-table-header-cel',
                    className,
                    {
                        'n2o-advanced-table-header-text-center': !alignment,
                        [`alignment-${alignment}`]: alignment,
                    },
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
            alignment,
        } = this.props
        let cellContent = null

        if (isString(title)) {
            cellContent = <TextTableHeader label={title} {...this.props} columnId={id} className="in-multi" />
        } else if (isString(children)) {
            return this.renderStringChild()
        } else if (multiHeader && isArray(children)) {
            return this.renderMultiCell()
        } else {
            cellContent = children
        }

        const ElementType = as || 'th'

        if (!cellContent && children === undefined) {
            return null
        }

        const visible = get(cellContent, 'props.visible', true)

        if (!visible) {
            return null
        }

        return (
            <ElementType
                title={label}
                rowSpan={rowSpan}
                colSpan={colSpan}
                style={style}
                className={classNames('n2o-advanced-table-header-cel', {
                    [selectionClass]: selectionHead,
                    'n2o-advanced-table-header-text-center': multiHeader && !alignment,
                    'd-none': !get(children, 'props.needRender', true),
                    [`alignment-${alignment}`]: alignment,
                })}
            >
                <div className={classNames('n2o-advanced-table-header-cell-content', className)}>
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
        const { width, onResize, resizable, needRender = true, visible = true } = this.props

        if (!visible || !needRender) {
            return null
        }

        return (
            <>
                {resizable && width ? (
                    <Resizable width={width} height={0} onResize={onResize} handleSize={[10, 10]}>
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
    as: PropTypes.any,
    icon: PropTypes.string,
    filterable: PropTypes.bool,
    filters: PropTypes.any,
    style: PropTypes.any,
    component: PropTypes.any,
    colSpan: PropTypes.any,
    rowSpan: PropTypes.any,
    children: PropTypes.oneOfType([PropTypes.array, PropTypes.string, PropTypes.object]),
    className: PropTypes.string,
    id: PropTypes.string,
    label: PropTypes.string,
    multiHeader: PropTypes.bool,
    onFilter: PropTypes.func,
    onResize: PropTypes.func,
    // eslint-disable-next-line react/no-unused-prop-types
    onSort: PropTypes.func,
    sorting: PropTypes.object,
    title: PropTypes.oneOfType([PropTypes.string, PropTypes.func, PropTypes.node]),
    width: PropTypes.number,
    resizable: PropTypes.bool,
    selectionHead: PropTypes.bool,
    selectionClass: PropTypes.string,
    filterControl: PropTypes.object,
    alignment: PropTypes.oneOf(['right', 'center', 'left']),
    needRender: PropTypes.bool,
    visible: PropTypes.bool,
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
