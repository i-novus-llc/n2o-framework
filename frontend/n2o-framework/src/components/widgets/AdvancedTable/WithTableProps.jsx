import React, { useMemo, useContext } from 'react'
import omit from 'lodash/omit'
import { useStore } from 'react-redux'

import { FactoryContext } from '../../../core/factory/context'
import { StandardFieldset } from '../Form/fieldsets'
import { dataSourceValidationSelector } from '../../../ducks/datasource/selectors'
import { ValidationsKey } from '../../../core/validation/types'

import { useColumnsState } from './hooks/useColumnsState'
import { useResolveCellsVisible } from './hooks/useResolveCellsVisible'

export function WithTableProps(Component) {
    function Wrapper(props) {
        const { getState } = useStore()
        const state = getState()

        const { filter, id, table, datasourceModelLength, datasource } = props
        const { resolveProps } = useContext(FactoryContext)
        const { header, body } = table

        const cells = useMemo(() => ({
            header: header.cells.map(cell => resolveProps(cell)),
            body: body.cells.map(cell => resolveProps(cell)),
        }), [body.cells, header.cells, resolveProps])

        const resolvedFilter = useMemo(() => resolveProps(filter, StandardFieldset), [filter, resolveProps])

        const [columnsState, changeColumnParam, switchTableParameter] = useColumnsState(cells.header, id, state)
        const resolvedCells = useResolveCellsVisible(cells, columnsState)

        const tableConfig = useMemo(() => {
            const config = omit(table, [
                'autoSelect',
                'autoFocus',
                'textWrap',
                'header.cells',
                'body.cells',
            ])

            /* WARNING! Неявное место, здесь собирается RowComponent (кастомизация) по src */
            return resolveProps(config)
        }, [resolveProps, table])

        const paginationVisible = useMemo(
            () => {
                if (datasourceModelLength === 0) { return false }

                return !columnsState.every(column => !column.visibleState || !column.visible)
            },
            [columnsState, datasourceModelLength],
        )

        const validations = dataSourceValidationSelector(datasource, ValidationsKey.FilterValidations)(state) || {}

        return (
            <Component
                {...props}
                changeColumnParam={changeColumnParam}
                columnsState={columnsState}
                tableConfig={tableConfig}
                resolvedFilter={resolvedFilter}
                resolvedCells={resolvedCells}
                paginationVisible={paginationVisible}
                switchTableParam={switchTableParameter}
                validations={validations}
            />
        )
    }

    return Wrapper
}
