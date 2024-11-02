import React, { useCallback, useMemo } from 'react'
import { useStore } from 'react-redux'
import classNames from 'classnames'
import get from 'lodash/get'
import debounce from 'lodash/debounce'

import { Alert } from '../../snippets/Alerts/Alert'
import { PageTitle, PageTitle as DocumentTitle } from '../../core/PageTitle'
import { BreadcrumbContainer } from '../../core/Breadcrumb/BreadcrumbContainer'
import { Toolbar } from '../../buttons/Toolbar'
import { PageRegions } from '../PageRegions'
import SearchBar from '../../snippets/SearchBar/SearchBar'
import { FILTER_DELAY } from '../../../constants/time'
import { dataSourceModelsSelector } from '../../../ducks/datasource/selectors'
import { dataRequest } from '../../../ducks/datasource/store'
import { setModel } from '../../../ducks/models/store'
import { usePageRegister } from '../usePageRegister'
import { ModelPrefix } from '../../../core/datasource/const'
import { SearchablePageProps } from '../types'

function SearchablePage({
    id,
    metadata,
    pageId,
    error,
    regions,
    disabled,
    initSearchValue,
    dispatch,
    withToolbar = true,
}: SearchablePageProps) {
    const { style, className, datasources, page,
        breadcrumb, searchBar, toolbar = {} } = metadata || {}

    usePageRegister(dispatch, datasources, pageId)

    const { title, htmlTitle,
        datasource, model: modelPrefix } = page || {}

    const store = useStore()
    const state = store.getState()

    const searchDatasource = get(searchBar, 'datasource') || get(metadata, 'searchWidgetId')
    const { filter } = dataSourceModelsSelector(searchDatasource as string)(state)
    const fieldId = get(searchBar, 'fieldId') || get(metadata, 'searchModelKey')
    const filterValue = get(filter, fieldId)

    const handleSearch = useCallback((value: string) => {
        const model = { ...filter, [fieldId]: value }

        dispatch(setModel(ModelPrefix.filter, searchDatasource, model))
        dispatch(dataRequest(searchDatasource))
    }, [dispatch, fieldId, filter, searchDatasource])

    const onSearch = useMemo(() => debounce(handleSearch, FILTER_DELAY), [handleSearch])

    return (
        <div
            style={style}
            className={classNames(
                'n2o-page n2o-page__searchable-page n2o-searchable-page',
                className,
                { 'n2o-disabled-page': disabled },
            )
        }
        >
            {error && <Alert {...error} visible />}
            <DocumentTitle htmlTitle={htmlTitle} datasource={datasource} modelPrefix={modelPrefix} />
            <div className="n2o-searchable-page__breadcrumbs">
                <BreadcrumbContainer breadcrumb={breadcrumb} datasource={datasource} modelPrefix={modelPrefix} />
            </div>
            <div className="n2o-searchable-page__title d-flex align-items-center my-3">
                <PageTitle title={title} datasource={datasource} modelPrefix={modelPrefix} className="mr-0" />
                <SearchBar
                    {...searchBar}
                    initialValue={filterValue}
                    className={classNames('ml-auto', searchBar?.className)}
                    onSearch={onSearch}
                    initSearchValue={initSearchValue}
                />
            </div>
            <div className="n2o-page-actions">
                <Toolbar className="ml-3" entityKey={pageId} toolbar={toolbar.topLeft} />
                <Toolbar className="ml-3" entityKey={pageId} toolbar={toolbar.topCenter} />
                <Toolbar className="ml-3" entityKey={pageId} toolbar={toolbar.topRight} />
            </div>
            <PageRegions id={id} regions={regions} />
            {withToolbar && (
                <div className="n2o-page-actions">
                    <Toolbar className="ml-3" entityKey={pageId} toolbar={toolbar.bottomLeft} />
                    <Toolbar className="ml-3" entityKey={pageId} toolbar={toolbar.bottomCenter} />
                    <Toolbar className="ml-3" entityKey={pageId} toolbar={toolbar.bottomRight} />
                </div>
            )}
        </div>
    )
}

export { SearchablePage }
export default SearchablePage
