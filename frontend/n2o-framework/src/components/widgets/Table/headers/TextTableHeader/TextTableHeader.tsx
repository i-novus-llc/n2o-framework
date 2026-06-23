import React, { Fragment, useCallback, useEffect } from 'react'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'
import classNames from 'classnames'

import { Sorter } from '../../../../snippets/Sorter/Sorter'
import { changeFrozenColumn, changeColumnVisibility } from '../../../../../ducks/columns/store'
import { useDataSourceMethodsContext } from '../../../../../core/widget/context'
import { type TextTableHeaderProps } from '../types'

/**
 * Текстовый заголовок таблицы с возможностью сортировки
 * @reactProps {string} [sortingParam] - параметр сортировки
 * @reactProps {string} soring - текущее направление сортировки
 * @reactProps {string} label - Текст заголовка столбца
 * @reactProps {function} onSort - эвент сортировки. Вызывает при смене направления сортировки
 */
export const TextTableHeader = ({
    sortingParam,
    sorting,
    label,
    style,
    dispatch,
    widgetId,
    columnId,
    visible,
    className,
}: TextTableHeaderProps) => {
    const { setSorting } = useDataSourceMethodsContext()

    const toggleVisibility = useCallback((visible: boolean) => {
        dispatch?.(changeColumnVisibility(widgetId, columnId, visible))
        dispatch?.(changeFrozenColumn(widgetId, columnId))
    }, [columnId, dispatch, widgetId])

    useEffect(() => {
        if (!visible) { toggleVisibility(visible) }
    }, [])

    const content = (
        <span className={classNames('n2o-advanced-table-header-title', className)} style={style}>
            <Text>{label}</Text>
        </span>
    )

    if (sortingParam) {
        return (
            <Sorter sorting={sorting} sortingParam={sortingParam} onSort={setSorting} className="n2o-advanced-table-header-sorter">
                {content}
            </Sorter>
        )
    }

    return content
}

export default TextTableHeader
