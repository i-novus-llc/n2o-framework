import React, { useMemo, useContext } from 'react'
import omit from 'lodash/omit'
import { useSelector, useStore } from 'react-redux'

import { FactoryContext } from '../../../core/factory/context'
import { StandardFieldset } from '../Form/fieldsets'
import { dataSourceValidationSelector } from '../../../ducks/datasource/selectors'
import { ValidationsKey } from '../../../core/validation/types'
import { State } from '../../../ducks/State'
import { getTableParam } from '../../../ducks/table/selectors'
import { getAllValuesByKey } from '../../Table/utils'

import { ChangeColumnParam, SwitchTableParam, useColumnsState } from './hooks/useColumnsState'
import { useResolveCellsVisible } from './hooks/useResolveCellsVisible'
import { type WithTableType, type BodyCells, type HeaderCells } from './types'

export function WithTableProps<P extends WithTableType>(Component: React.ComponentType<P>) {
    function Wrapper(props: P) {
        const { getState } = useStore()
        const state: State = getState()

        const { filter, id, table, datasourceModelLength, datasource, page } = props
        const { resolveProps } = useContext(FactoryContext)

        const { header, body } = table

        const cells = useMemo(() => ({
            header: header.cells.map(cell => resolveProps(cell)) as HeaderCells['cells'],
            body: body.cells.map(cell => resolveProps(cell)) as BodyCells['cells'],
        }), [header.cells, body.cells, resolveProps])

        const resolvedFilter = useMemo(() => resolveProps(filter, StandardFieldset), [filter, resolveProps])

        const [changeColumnParam, switchTableParam] = useColumnsState() as [
            changeColumnParam: ChangeColumnParam,
            switchTableParam: SwitchTableParam,
        ]

        // INFO Объект, содержащий все заголовки по ключу, соответствующему идентификатору колонки
        const columnsState = getAllValuesByKey(cells.header, { keyToIterate: 'children' })

        // INFO содержит только видимые колонки
        const resolvedCells = useResolveCellsVisible(cells, columnsState)

        const tableConfig = useMemo(() => {
            const config = omit(table, [
                'autoSelect',
                'autoFocus',
                'textWrap',
                'header.cells',
                'body.cells',
            ])

            // WARNING! Неявное место, здесь собирается RowComponent (кастомизация) по src
            return resolveProps(config)
        }, [resolveProps, table])

        const paginationVisible = useMemo(
            () => {
                if (datasourceModelLength === 0 && page === 1) { return false }

                return !columnsState.every(({ visibleState, visible }) => !visibleState || !visible)
            },
            [columnsState, datasourceModelLength, page],
        )

        const validations = dataSourceValidationSelector(datasource, ValidationsKey.FilterValidations)(state) || {}
        const textWrap = useSelector(getTableParam(id, 'textWrap'))

        return (
            <Component
                {...props}
                changeColumnParam={changeColumnParam}
                columnsState={columnsState}
                tableConfig={tableConfig}
                resolvedFilter={resolvedFilter}
                resolvedCells={resolvedCells}
                paginationVisible={paginationVisible}
                switchTableParam={switchTableParam}
                validations={validations}
                textWrap={textWrap}
            />
        )
    }

    return Wrapper
}
