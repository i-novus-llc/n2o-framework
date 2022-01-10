import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import { SORT_DIRECTION } from '../../../core/datasource/const'

/**
 * @param {SORT_DIRECTION} direction
 * @return {SORT_DIRECTION}
 */
function getNextDirection(direction) {
    switch (direction) {
        case SORT_DIRECTION.ASC: {
            return SORT_DIRECTION.DESC
        }
        case SORT_DIRECTION.DESC: {
            return SORT_DIRECTION.NONE
        }
        case SORT_DIRECTION.NONE: {
            return SORT_DIRECTION.ASC
        }
        default: {
            return SORT_DIRECTION.NONE
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
            'fa fa-sort-amount-asc': sorting === SORT_DIRECTION.ASC,
            'fa fa-sort-amount-desc': sorting === SORT_DIRECTION.DESC,
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
    sorting: PropTypes.oneOf(Object.values(SORT_DIRECTION)),
    sortingParam: PropTypes.string,
    onSort: PropTypes.func,
}

Sorter.defaultProps = {
    sorting: SORT_DIRECTION.NONE,
}

export default Sorter
