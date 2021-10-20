import React, { useMemo } from 'react'
import { connect } from 'react-redux'
import { batchActions } from 'redux-batched-actions'
import { compose, withHandlers, withProps, mapProps } from 'recompose'
import { createStructuredSelector } from 'reselect'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'
import get from 'lodash/get'
import PropTypes from 'prop-types'
import debounce from 'lodash/debounce'

import { updateModel } from '../../../ducks/models/store'
import { dataRequestWidget } from '../../../ducks/widgets/store'
import { makeGetModelByPrefixSelector } from '../../../ducks/models/selectors'
import { makeModelIdSelector } from '../../../ducks/widgets/selectors'
import Alert from '../../snippets/Alerts/Alert'
import DocumentTitle from '../../core/DocumentTitle'
import PageTitle from '../../core/PageTitle'
import DefaultBreadcrumb from '../../core/Breadcrumb/DefaultBreadcrumb'
import BreadcrumbContainer from '../../core/Breadcrumb/BreadcrumbContainer'
import Toolbar from '../../buttons/Toolbar'
import PageRegions from '../PageRegions'
// eslint-disable-next-line import/no-named-as-default
import SearchBar from '../../snippets/SearchBar/SearchBar'
import { FILTER_DELAY } from '../../../constants/time'

function SearchablePage({
    id,
    metadata,
    toolbar,
    pageId,
    error,
    regions,
    disabled,
    onSearch,
    searchBar,
    filterValue,
    withToolbar,
    initSearchValue,
}) {
    const { style, className } = metadata
    const searchHandler = useMemo(() => debounce(onSearch, FILTER_DELAY), [onSearch])

    return (
        <div
            className={classNames(
                'n2o-page n2o-page__searchable-page n2o-searchable-page',
                className,
                {
                    'n2o-disabled-page': disabled,
                },
            )}
            style={style}
        >
            {error && <Alert {...error} visible />}
            {!isEmpty(metadata) && metadata.page && (
                <DocumentTitle {...metadata.page} />
            )}
            <div className="n2o-searchable-page__breadcrumbs">
                {!isEmpty(metadata) && metadata.breadcrumb && (
                    <BreadcrumbContainer
                        defaultBreadcrumb={DefaultBreadcrumb}
                        items={metadata.breadcrumb}
                    />
                )}
            </div>
            <div className="n2o-searchable-page__title d-flex align-items-center my-3">
                <PageTitle className="mr-0" {...get(metadata, 'page', {})} />
                <Toolbar className="ml-2" entityKey={pageId} toolbar={toolbar.title} />
                <SearchBar
                    {...searchBar}
                    initialValue={filterValue}
                    className={classNames('ml-auto', searchBar.className)}
                    onSearch={searchHandler}
                    initSearchValue={initSearchValue}
                />
            </div>
            <div className="n2o-page-actions">
                <Toolbar
                    className="ml-3"
                    entityKey={pageId}
                    toolbar={toolbar.topLeft}
                />
                <Toolbar
                    className="ml-3"
                    entityKey={pageId}
                    toolbar={toolbar.topCenter}
                />
                <Toolbar
                    className="ml-3"
                    entityKey={pageId}
                    toolbar={toolbar.topRight}
                />
            </div>
            <PageRegions id={id} regions={regions} />
            {withToolbar && (
                <div className="n2o-page-actions">
                    <Toolbar
                        className="ml-3"
                        entityKey={pageId}
                        toolbar={toolbar.bottomLeft}
                    />
                    <Toolbar
                        className="ml-3"
                        entityKey={pageId}
                        toolbar={toolbar.bottomCenter}
                    />
                    <Toolbar
                        className="ml-3"
                        entityKey={pageId}
                        toolbar={toolbar.bottomRight}
                    />
                </div>
            )}
        </div>
    )
}

const mapStateToProps = createStructuredSelector({
    filterModel: (
        state,
        { searchModelPrefix, searchWidgetId },
    ) => makeGetModelByPrefixSelector(searchModelPrefix, searchWidgetId)(state),
    modelId: (state, { searchWidgetId }) => makeModelIdSelector(searchWidgetId)(state),
})

const enhance = compose(
    withProps(props => ({
        searchWidgetId: get(props, 'metadata.searchWidgetId'),
        searchModelPrefix: get(props, 'metadata.searchModelPrefix'),
        searchModelKey: get(props, 'metadata.searchModelKey'),
        searchBar: get(props, 'metadata.searchBar', {}),
        toolbar: get(props, 'metadata.toolbar', {}),
    })),
    withHandlers({
        onSearch: ({
            dispatch,
            searchWidgetId,
            searchModelPrefix,
            searchModelKey,
            modelId,
        }) => (value) => {
            dispatch(
                batchActions([
                    updateModel(searchModelPrefix, searchWidgetId, searchModelKey, value),
                    dataRequestWidget(searchWidgetId, modelId, { page: 1 }),
                ]),
            )
        },
    }),
    connect(mapStateToProps),
    mapProps(({ filterModel, searchModelKey, ...rest }) => ({
        ...rest,
        filterValue: get(filterModel, searchModelKey),
    })),
)

SearchablePage.propTypes = {
    id: PropTypes.string,
    metadata: PropTypes.object,
    toolbar: PropTypes.object,
    pageId: PropTypes.string,
    error: PropTypes.object,
    regions: PropTypes.object,
    disabled: PropTypes.bool,
    onSearch: PropTypes.func,
    searchBar: PropTypes.object,
    filterValue: PropTypes.string,
    withToolbar: PropTypes.bool,
    initSearchValue: PropTypes.string,
}

SearchablePage.defaultProps = {
    toolbar: {},
    searchBar: {},
    withToolbar: true,
}

export { SearchablePage }
export default enhance(SearchablePage)
