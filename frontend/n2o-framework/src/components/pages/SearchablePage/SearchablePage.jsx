import React, { useMemo } from 'react'
import { connect } from 'react-redux'
import { compose, withHandlers, withProps, mapProps } from 'recompose'
import { createStructuredSelector } from 'reselect'
import classNames from 'classnames'
import get from 'lodash/get'
import PropTypes from 'prop-types'
import debounce from 'lodash/debounce'
import { set } from 'lodash'

import Alert from '../../snippets/Alerts/Alert'
import { PageTitle, PageTitle as DocumentTitle } from '../../core/PageTitle'
import { BreadcrumbContainer } from '../../core/Breadcrumb/BreadcrumbContainer'
import Toolbar from '../../buttons/Toolbar'
import PageRegions from '../PageRegions'
// eslint-disable-next-line import/no-named-as-default
import SearchBar from '../../snippets/SearchBar/SearchBar'
import { FILTER_DELAY } from '../../../constants/time'
import { dataSourceModelsSelector } from '../../../ducks/datasource/selectors'
import { dataRequest } from '../../../ducks/datasource/store'
import { setModel } from '../../../ducks/models/store'
import { usePageRegister } from '../usePageRegister'
import { ModelPrefix } from '../../../core/datasource/const'

function SearchablePage({
    id,
    metadata = {},
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
    dispatch,
}) {
    const { style, className, datasources, page, breadcrumb } = metadata
    const { title, htmlTitle, datasource, model: modelPrefix } = page
    const searchHandler = useMemo(() => debounce(onSearch, FILTER_DELAY), [onSearch])

    usePageRegister(dispatch, datasources, pageId)

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
            <DocumentTitle htmlTitle={htmlTitle} datasource={datasource} modelPrefix={modelPrefix} />
            <div className="n2o-searchable-page__breadcrumbs">
                <BreadcrumbContainer
                    breadcrumb={breadcrumb}
                    datasource={datasource}
                    modelPrefix={modelPrefix}
                />
            </div>
            <div className="n2o-searchable-page__title d-flex align-items-center my-3">
                <PageTitle title={title} datasource={datasource} modelPrefix={modelPrefix} className="mr-0" />
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
    filterModel: (state, { datasource }) => dataSourceModelsSelector(datasource)(state),
})

const enhance = compose(
    withProps(props => ({
        searchBar: get(props, 'metadata.searchBar', {}),
        // fixme убрать || после того как бек поменяет формат данных на новый
        fieldId: get(props, 'metadata.searchBar.fieldId') || get(props, 'metadata.searchModelKey'),
        datasource: get(props, 'metadata.searchBar.datasource') || get(props, 'metadata.searchWidgetId'),
        toolbar: get(props, 'metadata.toolbar', {}),
    })),
    withHandlers({
        onSearch: ({
            dispatch,
            datasource,
            filterModel,
            fieldId,
        }) => (value) => {
            const newModel = { ...filterModel }

            set(newModel, fieldId, value)
            dispatch(setModel(ModelPrefix.filter, datasource, newModel))
            dispatch(dataRequest(datasource))
        },
    }),
    connect(mapStateToProps),
    mapProps(({ filterModel, fieldId, ...rest }) => ({
        ...rest,
        filterValue: get(filterModel, fieldId),
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
    dispatch: PropTypes.func,
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
