import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import { SortDirection } from '../../../core/datasource/const'

/**
 * @param {SortDirection} direction
 * @return {SortDirection}
 */
function getNextDirection(direction) {
    switch (direction) {
        case SortDirection.asc: {
            return SortDirection.desc
        }
        case SortDirection.desc: {
            return SortDirection.none
        }
        case SortDirection.none: {
            return SortDirection.asc
        }
        default: {
            return SortDirection.none
        }
    }
}

/**
 * Компонент сортировки. Оборачивает элемент, добавляя логику и иконки
 * @reactProps {element} children - Потомки компонента, которые будут обернуты
 * @reactProps {string} title - подска при наведение
 * @reactProps {string} sorting - текущее направление сортировки (ASC, DESC, NONE)
 * @reactProps {string} sortingParam - параметр сортировки
 * @reactProps {function} onSort - callback вызывается при смене сортировки
 * @example
 * <Sorter title="Hello"
 *             sorting="ASC"
 *             onSort={this.changeSortDirection}>
 *  World
 * </Sorter>
 */
export class Sorter extends React.Component {
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
        const { sorting, sortingParam, onSort } = this.props
        const direction = getNextDirection(sorting)

        onSort(sortingParam, direction)
    }

    render() {
        const { title, children, sorting } = this.props
        const iconClass = classNames({
            'fa fa-sort-amount-asc': sorting === SortDirection.asc,
            'fa fa-sort-amount-desc': sorting === SortDirection.desc,
        })

        return (
            // eslint-disable-next-line jsx-a11y/anchor-is-valid
            <a href="#" title={title} tabIndex={-1} onClick={this.handleClick}>
                {children}
                {' '}
                {iconClass && (
                    <i className={classNames('n2o-sorting-icon', iconClass)} aria-hidden="true" />
                )}
            </a>
        )
    }
}

Sorter.propTypes = {
    children: PropTypes.node.isRequired,
    title: PropTypes.string,
    sorting: PropTypes.oneOf(Object.values(SortDirection)),
    sortingParam: PropTypes.string,
    onSort: PropTypes.func,
}

Sorter.defaultProps = {
    sorting: SortDirection.none,
}

export default Sorter
