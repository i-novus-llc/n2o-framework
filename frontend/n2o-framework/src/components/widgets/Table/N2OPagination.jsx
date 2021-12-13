import React, { Component } from 'react'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'

import Pagination from '../../snippets/Pagination/Pagination'

/**
 * Компонент табличной пейджинации. По `widgetId` автоматически определяет все свойства для `Paging`
 * @reactProps {string} widgetId - уникальный индефикатор виджета
 * @reactProps {number} count
 * @reactProps {number} size
 * @reactProps {number} activePage
 * @reactProps {function} onChangePage
 */
export class N2OPagination extends Component {
    componentDidUpdate(prevProps) {
        const { datasource, setPage, activePage, count, size } = this.props

        if (
            datasource &&
            !isEqual(prevProps.datasource, datasource) &&
            (isEmpty(datasource) && count > 0 && activePage > 1)
        ) {
            setPage(Math.ceil(count / size))
        }
    }

    render() {
        const {
            count,
            datasource,
            setPage,
            showCount,
        } = this.props

        const currentShowCount = typeof showCount === 'boolean' ? showCount : !isEmpty(datasource)

        return (
            count > 0 && (
                <Pagination
                    {...this.props}
                    onSelect={setPage}
                    showCount={currentShowCount}
                />
            )
        )
    }
}

N2OPagination.propTypes = {
    count: PropTypes.number,
    size: PropTypes.number,
    activePage: PropTypes.number,
    setPage: PropTypes.func,
    datasource: PropTypes.array,
    maxPages: PropTypes.number,
    layout: PropTypes.oneOf([
        'bordered',
        'flat',
        'separated',
        'flat-rounded',
        'bordered-rounded',
        'separated-rounded',
    ]),
    prev: PropTypes.bool,
    prevIcon: PropTypes.string,
    prevLabel: PropTypes.string,
    next: PropTypes.bool,
    nextIcon: PropTypes.string,
    nextLabel: PropTypes.string,
    first: PropTypes.bool,
    firstIcon: PropTypes.string,
    firstLabel: PropTypes.string,
    last: PropTypes.bool,
    lastIcon: PropTypes.string,
    lastLabel: PropTypes.string,
    showCount: PropTypes.bool,
    showSinglePage: PropTypes.bool,
    className: PropTypes.string,
    style: PropTypes.object,
}
