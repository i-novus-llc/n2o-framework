import React, { useContext } from 'react'
import isEmpty from 'lodash/isEmpty'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { type Props as PaginationSnippetProps } from '../../snippets/Pagination/constants'
import { DataSourceModels } from '../../../core/datasource/const'

export interface Props extends PaginationSnippetProps {
    datasource: DataSourceModels['datasource']
    setPage: PaginationSnippetProps['onSelect']
}

/**
 * Компонент табличной пейджинации. По `widgetId` автоматически определяет все свойства для `Paging`
 * @reactProps {string} widgetId - уникальный индефикатор виджета
 * @reactProps {number} count
 * @reactProps {number} size
 * @reactProps {number} activePage
 * @reactProps {function} onChangePage
 */
export const N2OPagination = (props: Props) => {
    const {
        datasource,
        setPage,
        showCount: propShowCount,
        loading,
        hasNext,
        count,
    } = props
    const { getComponent } = useContext(FactoryContext)
    const showCount = propShowCount || !isEmpty(datasource)
    const calculatedHasNext = count ? hasNext : (!loading && hasNext)

    const Pagination = getComponent('Pagination', FactoryLevels.SNIPPETS)

    if (!Pagination) { return null }

    return (
        <Pagination
            {...props}
            onSelect={setPage}
            showCount={showCount}
            hasNext={calculatedHasNext}
            loading={loading}
            showSinglePage={false}
        />
    )
}

N2OPagination.displayName = 'N2OPagination'
