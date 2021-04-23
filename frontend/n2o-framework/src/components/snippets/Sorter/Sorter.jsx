import React from 'react'
import PropTypes from 'prop-types'
import cx from 'classnames'

const SORT_TYPE = {
    ASC: 'ASC',
    DESC: 'DESC',
    NONE: 'NONE',
}

/**
 * Компонент сортировки. Оборачивает элемент, добавляя логику и иконки
 * @reactProps {element} children - Потомки компонента, которые будут обернуты
 * @reactProps {string} title - подска при наведение
 * @reactProps {string} sorting - текущее направление сортировки (ASC, DESC, NONE)
 * @reactProps {number} columnKey - идентификатор колонки
 * @reactProps {function} onSort - callback вызывается при смене сортировки
 * @example
 * <Sorter title="Hello"
 *             sorting="ASC"
 *             onSort={this.changeSortDirection}>
 *  World
 * </Sorter>
 */
class Sorter extends React.Component {
    constructor(props) {
        super(props)
        this.handleClick = this.handleClick.bind(this)
    }

    /**
   * Обработчик нажатия и вызов callback onSort
   * @param e
   */
    handleClick(e) {
        e.preventDefault()
        const { sorting, columnKey, onSort } = this.props
        let direction
        switch (sorting) {
            case SORT_TYPE.ASC:
                direction = SORT_TYPE.DESC
                break
            case SORT_TYPE.DESC:
                direction = SORT_TYPE.NONE
                break
            case SORT_TYPE.NONE:
                direction = SORT_TYPE.ASC
                break
            default:
                direction = SORT_TYPE.NONE
        }
        onSort(columnKey, direction)
    }

    /**
   * Базовый рендер компонента
   */
    render() {
        const { title, children, sorting } = this.props
        const iconClass = cx({
            'fa fa-sort-amount-asc': sorting === SORT_TYPE.ASC,
            'fa fa-sort-amount-desc': sorting === SORT_TYPE.DESC,
        })
        return (
            <a href="#" title={title} tabIndex={-1} onClick={this.handleClick}>
                {children}
                {' '}
                {iconClass && (
                    <i className={cx('n2o-sorting-icon', iconClass)} aria-hidden="true" />
                )}
            </a>
        )
    }
}

Sorter.propTypes = {
    children: PropTypes.node.isRequired,
    title: PropTypes.string,
    sorting: PropTypes.oneOf(Object.keys(SORT_TYPE)),
    columnKey: PropTypes.string,
    onSort: PropTypes.func,
}

Sorter.defaultProps = {
    sorting: 'NONE',
}

export default Sorter
