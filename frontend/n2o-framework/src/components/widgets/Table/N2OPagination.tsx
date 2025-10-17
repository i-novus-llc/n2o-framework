import React, { useContext, useEffect, useState, useMemo } from 'react'
import { useStore } from 'react-redux'
import isEmpty from 'lodash/isEmpty'
import isNil from 'lodash/isNil'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { COUNT_NEVER, COUNT_BY_REQUEST, type Props as PaginationSnippetProps } from '../../snippets/Pagination/constants'
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

    const filterDependency = useMemo(() => {
        return filterModel ? JSON.stringify(filterModel) : null
    }, [filterModel])

    const { getComponent } = useContext(FactoryContext)
    const [count, setCount] = useState(propsCount)

    /** FIXME костылек
     *  default count установлен в 0, при loading = false полученный count приходит с задержкой
     *  Из за этого бывают мерцания кнопки Узнать кол-во записей */
    const [showCountButton, setShowCountButton] = useState(false)

    useEffect(() => {
        let timer: ReturnType<typeof setTimeout>

        // показ кнопки сразу если count не ожидается (по запросу)
        if (propShowCount === COUNT_BY_REQUEST) {
            return setShowCountButton(true)
        }

        if (!loading && (Number(count) >= 0)) {
            // показ кнопки с задержкой после окончания загрузки
            timer = setTimeout(() => { setShowCountButton(true) }, 300)
        }

        return () => {
            if (timer) {
                clearTimeout(timer)
            }
        }
    }, [loading, count, propShowCount])

    useEffect(() => {
        if (filterDependency !== null) { setCount(null) }
    }, [filterDependency])

    useEffect(() => {
        if (!isNil(propsCount)) { setCount(propsCount) }
    }, [propsCount])

    const Pagination = getComponent('Pagination', FactoryLevels.SNIPPETS)

    if (!Pagination) { return null }

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

    const showCount = propShowCount !== COUNT_NEVER && !isEmpty(datasource)
    const calculatedHasNext = count ? hasNext : (!loading && hasNext)

    return (
        <section className="pagination-container">
            <Count
                count={count}
                visible={showCount && !loading}
                onClick={onClick}
                showCountButton={showCountButton}
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
