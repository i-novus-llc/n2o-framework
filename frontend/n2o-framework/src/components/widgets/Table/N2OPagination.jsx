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
} from '../../../selectors/widgets'
import {
    makeGetModelByPrefixSelector,
    makeGetFilterModelSelector,
} from '../../../selectors/models'
import { dataRequestWidget } from '../../../actions/widgets'
import { PREFIXES } from '../../../constants/models'

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
        } = this.props

        const onSelect = page => onChangePage(page, { ...filters })

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
                    showCount={showCount}
                    showSinglePage={showSinglePage}
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
    layout: PropTypes.string,
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
                dataRequestWidget(ownProps.widgetId, {
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

export default N2OPaginationComponent
