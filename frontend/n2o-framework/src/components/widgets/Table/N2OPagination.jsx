import React, { Component } from 'react'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'

import { Pagination } from '../../snippets/Pagination/Pagination'

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
            datasource,
            setPage,
            showCount: propShowCount,
            loading,
            hasNext,
            count,
        } = this.props

        const showCount = propShowCount || !isEmpty(datasource)
        const calculatedHasNext = count ? hasNext : (!loading && hasNext)

        return (
            <Pagination
                {...this.props}
                onSelect={setPage}
                showCount={showCount}
                hasNext={calculatedHasNext}
                loading={loading}
            />
        )
    }
}

N2OPagination.propTypes = {
    count: PropTypes.number,
    size: PropTypes.number,
    activePage: PropTypes.number,
    setPage: PropTypes.func,
    datasource: PropTypes.array,
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
    next: PropTypes.bool,
    nextIcon: PropTypes.string,
    last: PropTypes.bool,
    lastIcon: PropTypes.string,
    showCount: PropTypes.bool,
    className: PropTypes.string,
    style: PropTypes.object,
}
