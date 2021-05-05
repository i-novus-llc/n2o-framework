import React from 'react'
import { connect } from 'react-redux'
import { batchActions } from 'redux-batched-actions'
import { compose, withHandlers, withProps, mapProps } from 'recompose'
import { createStructuredSelector } from 'reselect'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'
import get from 'lodash/get'

import { updateModel } from '../../../actions/models'
import { dataRequestWidget } from '../../../actions/widgets'
import { makeGetModelByPrefixSelector } from '../../../selectors/models'
import Alert from '../../snippets/Alerts/Alert'
import DocumentTitle from '../../core/DocumentTitle'
import PageTitle from '../../core/PageTitle'
import DefaultBreadcrumb from '../../core/Breadcrumb/DefaultBreadcrumb'
import BreadcrumbContainer from '../../core/Breadcrumb/BreadcrumbContainer'
import Toolbar from '../../buttons/Toolbar'
import PageRegions from '../PageRegions'
import SearchBar from '../../snippets/SearchBar/SearchBar'

function SearchablePage({
    id,
    metadata,
    toolbar = {},
    pageId,
    error,
    regions,
    disabled,
    onSearch,
    searchBar = {},
    filterValue,
    withToolbar = true,
    initSearchValue,
}) {
    const { style, className } = metadata

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
                    onSearch={onSearch}
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
})

const enhance = compose(
    withProps((props) => {
        const isDrawerPage = get(props, 'isDrawerPage')
        const pageId = get(props, 'metadata.id')
        const searchWidgetId = get(props, 'metadata.searchWidgetId')
        const compileSearchWidgetId = isDrawerPage
            ? `${pageId}_${searchWidgetId}`
            : searchWidgetId

        return {
            searchWidgetId: compileSearchWidgetId,
            metadataSearchWidgetId: searchWidgetId,
            searchModelPrefix: get(props, 'metadata.searchModelPrefix'),
            isDrawerPage,
            searchModelKey: get(props, 'metadata.searchModelKey'),
            searchBar: get(props, 'metadata.searchBar', {}),
            toolbar: get(props, 'metadata.toolbar', {}),
        }
    }),
    withHandlers({
        onSearch: ({
            dispatch,
            searchWidgetId,
            metadataSearchWidgetId,
            searchModelPrefix,
            searchModelKey,
        }) => (value) => {
            dispatch(
                batchActions([
                    updateModel(
                        searchModelPrefix,
                        metadataSearchWidgetId,
                        searchModelKey,
                        value,
                    ),
                    dataRequestWidget(searchWidgetId),
                    dataRequestWidget(metadataSearchWidgetId),
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

export { SearchablePage }
export default enhance(SearchablePage)
