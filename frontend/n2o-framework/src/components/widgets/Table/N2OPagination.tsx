import React, { useContext, useEffect, useState } from 'react'
import { useStore } from 'react-redux'
import isEmpty from 'lodash/isEmpty'
import isNil from 'lodash/isNil'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { COUNT_NEVER, type Props as PaginationSnippetProps } from '../../snippets/Pagination/constants'
import { DataSourceModels, ModelPrefix } from '../../../core/datasource/const'
import request from '../../../utils/request'
import { type Paging } from '../../../ducks/datasource/Provider'
import { getModelByPrefixAndNameSelector } from '../../../ducks/models/selectors'
import { dataProviderResolver } from '../../../core/dataProviderResolver'

import { Count } from './Count'

export interface Props extends PaginationSnippetProps {
    datasource: DataSourceModels['datasource']
    datasourceId: string
    countDataProvider?: Paging['countDataProvider']
    setPage: PaginationSnippetProps['onSelect']
}

/**
 * Компонент табличной пейджинации
 */
export const N2OPagination = (props: Props) => {
    const {
        datasource,
        datasourceId,
        setPage,
        loading,
        hasNext,
        countDataProvider,
        showCount: propShowCount,
        count: propsCount = null,
        size,
    } = props
    const { getState } = useStore()
    const state = getState()
    const filterModel = getModelByPrefixAndNameSelector(ModelPrefix.filter, datasourceId)(state)
    const filterDependency = JSON.stringify(filterModel)

    const { getComponent } = useContext(FactoryContext)
    const [count, setCount] = useState(propsCount)

    const Pagination = getComponent('Pagination', FactoryLevels.SNIPPETS)

    useEffect(() => {
        if (!isNil(propsCount)) { setCount(propsCount) }
    }, [propsCount])

    useEffect(() => {
        setCount(null)
    }, [filterDependency])

    const onClick = async () => {
        if (!countDataProvider) { return }

        const { url } = dataProviderResolver(state, {
            ...countDataProvider,
            url: `${countDataProvider.url}/${datasourceId}`,
        })

        // TODO request,js не типизирован
        // eslint-disable-next-line @typescript-eslint/await-thenable
        const count = await request(url)

        // TODO request.js не типизирован
        setCount(count as never)
    }

    if (!Pagination) { return null }

    const showCount = propShowCount !== COUNT_NEVER && !isEmpty(datasource)
    const calculatedHasNext = count ? hasNext : (!loading && hasNext)

    return (
        <section className="pagination-container">
            <Count
                count={count}
                visible={showCount && !loading}
                onClick={onClick}
            />
            <Pagination
                {...props}
                onSelect={setPage}
                showCount={showCount}
                hasNext={calculatedHasNext}
                loading={loading}
                showSinglePage={false}
                showLast={!!count}
                count={count}
                size={size}
            />
        </section>
    )
}

N2OPagination.displayName = 'N2OPagination'
