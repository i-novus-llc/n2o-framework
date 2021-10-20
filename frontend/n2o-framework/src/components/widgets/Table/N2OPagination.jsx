import React, { Component } from 'react'
import PropTypes from 'prop-types'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'

import Pagination from '../../snippets/Pagination/Pagination'
import {
    makeWidgetCountSelector,
    makeWidgetSizeSelector,
    makeWidgetPageSelector,
} from '../../../ducks/widgets/selectors'
import {
    makeGetModelByPrefixSelector,
    makeGetFilterModelSelector,
} from '../../../ducks/models/selectors'
import { dataRequestWidget } from '../../../ducks/widgets/store'
import { PREFIXES } from '../../../ducks/models/constants'

/**
 * Компонент табличной пейджинации. По `widgetId` автоматически определяет все свойства для `Paging`
 * @reactProps {string} widgetId - уникальный индефикатор виджета
 * @reactProps {number} count
 * @reactProps {number} size
 * @reactProps {number} activePage
 * @reactProps {function} onChangePage
 */
class N2OPagination extends Component {
    componentDidUpdate(prevProps) {
        const { datasource, onChangePage, activePage, count, size } = this.props

        if (
            datasource &&
      !isEqual(prevProps.datasource, datasource) &&
      (isEmpty(datasource) && count > 0 && activePage > 1)
        ) {
            onChangePage(Math.ceil(count / size))
        }
    }

    render() {
        const {
            count,
            datasource,
            size,
            activePage,
            onChangePage,
            layout,
            prev,
            prevLabel,
            prevIcon,
            next,
            nextLabel,
            nextIcon,
            first,
            firstLabel,
            firstIcon,
            last,
            lastLabel,
            lastIcon,
            showCount,
            showSinglePage,
            maxPages,
            filters,
            className,
            style,
        } = this.props

        const onSelect = page => onChangePage(page, { ...filters })
        const currentShowCount = typeof showCount === 'boolean' ? showCount : !isEmpty(datasource)

        return (
            count > 0 && (
                <Pagination
                    onSelect={onSelect}
                    activePage={activePage}
                    count={count}
                    size={size}
                    maxPages={maxPages}
                    layout={layout}
                    prev={prev}
                    prevLabel={prevLabel}
                    prevIcon={prevIcon}
                    next={next}
                    nextLabel={nextLabel}
                    nextIcon={nextIcon}
                    first={first}
                    firstLabel={firstLabel}
                    firstIcon={firstIcon}
                    last={last}
                    lastLabel={lastLabel}
                    lastIcon={lastIcon}
                    showCount={currentShowCount}
                    showSinglePage={showSinglePage}
                    className={className}
                    style={style}
                />
            )
        )
    }
}

N2OPagination.propTypes = {
    count: PropTypes.number,
    size: PropTypes.number,
    activePage: PropTypes.number,
    onChangePage: PropTypes.func,
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
    filters: PropTypes.object,
    className: PropTypes.string,
    style: PropTypes.object,
}

N2OPagination.defaultProps = {
    datasource: [],
}

const mapStateToProps = createStructuredSelector({
    count: (state, props) => makeWidgetCountSelector(props.widgetId)(state, props),
    size: (state, props) => makeWidgetSizeSelector(props.widgetId)(state, props),
    activePage: (state, props) => makeWidgetPageSelector(props.widgetId)(state, props),
    datasource: (state, props) => makeGetModelByPrefixSelector(PREFIXES.datasource, props.widgetId)(
        state,
        props,
    ),
    filters: (state, props) => makeGetFilterModelSelector(props.widgetId)(state, props),
})

function mapDispatchToProps(dispatch, ownProps) {
    return {
        onChangePage: (page, filters) => {
            dispatch(
                dataRequestWidget(ownProps.widgetId, ownProps.modelId, {
                    page,
                    ...filters,
                }),
            )
        },
    }
}

const N2OPaginationComponent = connect(
    mapStateToProps,
    mapDispatchToProps,
)(N2OPagination)

export const getN2OPagination = (paging, place, widgetId, modelId) => (
    paging ? { [place]: <N2OPaginationComponent widgetId={widgetId} modelId={modelId} {...paging} /> } : {})

export default N2OPaginationComponent
